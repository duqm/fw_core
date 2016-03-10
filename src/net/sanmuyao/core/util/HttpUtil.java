package net.sanmuyao.core.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

/**
 * http工具
 *
 * @author 杜庆明
 *
 */
public class HttpUtil {
    private static Logger LOG = LogManager.getLogger(HttpUtil.class);
    private static HostnameVerifier sslHostnameVerifier;

    private static synchronized void initSslHostnameVerifier() {
        if (sslHostnameVerifier != null) {
            return;
        }
        sslHostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String urlHostName, SSLSession session) {
                return urlHostName != null && urlHostName.equals(session.getPeerHost());
            }
        };
    }

    private static SSLSocketFactory sslSocketFactory;

    /**
     * Ignore SSL Certificate
     */
    private static synchronized void initSslSocketFactory() {
        if (sslSocketFactory != null) {
            return;
        }
        InputStream in = null;
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            final X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    // do nothing
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    // do nothing
                }
            };
            context.init(null, new TrustManager[] {trustManager}, null);
            sslSocketFactory = context.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static HttpURLConnection connect(String url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        if ("https".equalsIgnoreCase(url.substring(0, 5))) {
            if (sslSocketFactory == null) {
                initSslSocketFactory();
            }
            ((HttpsURLConnection) conn).setSSLSocketFactory(sslSocketFactory);
            if (sslHostnameVerifier == null) {
                initSslHostnameVerifier();
            }
            ((HttpsURLConnection) conn).setHostnameVerifier(sslHostnameVerifier);
        }
        conn.setUseCaches(false);
        conn.setInstanceFollowRedirects(true);
        conn.setRequestProperty("Connection", "close");
        return conn;
    }

    /**
     * Closes the connection if opened.
     *
     * @param httpConn
     */
    private static void disconnect(HttpURLConnection httpConn) {
        if (httpConn != null) {
            httpConn.disconnect();
        }
    }

    /**
     * Returns content from the server's response.
     *
     * @param httpConn
     * @return
     * @throws IOException
     */
    private static String readRespone(HttpURLConnection httpConn) throws IOException {
        if (httpConn == null)
            return null;

        InputStream input = null;
        try {
            input = httpConn.getInputStream();
            return IOUtils.toString(input);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    /**
     * Returns http query string from param map.
     *
     * @param params
     * @return
     */
    public static String map2UrlParams(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Entry<String, String> entry : params.entrySet()) {
            if (!StringUtils.isEmpty(entry.getValue())) {
                String key = entry.getKey();
                try {
                    String value = URLEncoder.encode(entry.getValue(), "UTF-8");
                    sb.append("&" + key + "=" + value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (sb.length() > 0) {
            return sb.substring(1);
        }
        return null;
    }

    /**
     * Returns http url with parameters.
     *
     * @param requestURL
     * @param params
     * @return
     */
    public static String makeUrl(String requestURL, Map<String, String> params) {
        if (requestURL == null || requestURL.isEmpty()) {
            return null;
        }

        String queryString = map2UrlParams(params);
        if (!requestURL.contains("?")) {
            requestURL += '?';
        }
        requestURL += queryString;

        return requestURL;
    }

    /**
     * Returns content from the server's response via GET.
     *
     * @param requestURL
     * @return
     * @throws Exception
     */
    public static String get(String requestURL) throws Exception {
        LOG.debug("http get: " + requestURL);

        HttpURLConnection httpConn = null;
        String response = null;
        try {
            httpConn = connect(requestURL);
            httpConn.setRequestMethod("GET");
            httpConn.setDoInput(true);
            httpConn.setDoOutput(false);
            response = readRespone(httpConn);
            return response;
        } finally {
            disconnect(httpConn);
            LOG.debug("http response: " + response);
        }
    }
    
    /**
     * post
     * @param url
     * @param body
     * @return
     */
    public static String post(String url, String body) {
        URL u = null;
        HttpURLConnection con = null;

        // 尝试发送请求
        try {
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(body);
            osw.flush();
            osw.close();
        } catch (Exception e) {
            System.out.println("POST URL：" + url);
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        // 读取返回内容
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
                buffer.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * post 数据
     *
     * @param url
     * @param params
     * @return
     */
    public static String post(String url, Map<String, Object> params) {
        URL u = null;
        HttpURLConnection con = null;
        // 构建请求参数
        StringBuffer sb = new StringBuffer();
        if (params != null) {
            for (Entry<String, Object> e : params.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                sb.append("&");
            }
            sb.substring(0, sb.length() - 1);
        }
        System.out.println("send_url:" + url);
        System.out.println("send_data:" + sb.toString());
        // 尝试发送请求
        try {
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(sb.toString());
            osw.flush();
            osw.close();
        } catch (Exception e) {
            System.out.println("POST URL：" + url);
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        // 读取返回内容
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
                buffer.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }


    /**
     * 通过post发送文件
     *
     * @param url
     * @param mMap
     * @param fileMap
     * @return
     */
    public static String forwardFile(String url, Map<String, String[]> mMap,
            Map<String, List<MultipartFile>> fileMap) {
        try {
            String BOUNDARY = UUID.randomUUID().toString(); // 分隔符
            String PREFIX = "--", LINEND = "\r\n";
            String MULTIPART_FROM_DATA = "multipart/form-data";

            // 设置HTTP头
            URL requestUrl = new URL(url);
            HttpURLConnection hc = (HttpURLConnection) requestUrl.openConnection();
            hc.setRequestMethod("POST");
            hc.setDoOutput(true);
            hc.setDoInput(true);
            hc.setUseCaches(false);
            hc.setRequestProperty("Connection", "Keep-alive");
            hc.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

            StringBuffer sb = new StringBuffer();
            // 拼接参数
            String[] values = null;
            String value = "";
            for (Map.Entry<String, String[]> entry : mMap.entrySet()) {
                value = "";
                for (String val : values) {
                    if (value.length() > 0) {
                        value += ",";
                    }
                    value += val;
                }
                sb = sb.append(PREFIX);
                sb = sb.append(BOUNDARY);
                sb = sb.append(LINEND);
                sb =
                        sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\""
                                + LINEND + LINEND);
                values = entry.getValue();
                sb = sb.append(value);
                sb = sb.append(LINEND);
            }
            OutputStream output = hc.getOutputStream();
            byte[] data = sb.toString().getBytes();
            output.write(data);

            // 拼接文件
            MultipartFile entry = null;
            for (Entry<String, List<MultipartFile>> entrys : fileMap.entrySet()) {
                StringBuilder sbFile = new StringBuilder();
                if (entrys.getValue().size() > 0) {
                    // 只止传第一个文件
                    entry = entrys.getValue().get(0);
                }
                // 文件名进行转码，防止中文乱码
                String filename = entry.getOriginalFilename();
                sbFile = sbFile.append(PREFIX);
                sbFile = sbFile.append(BOUNDARY);
                sbFile = sbFile.append(LINEND);
                sbFile =
                        sbFile.append("Content-Disposition: form-data; name=\"" + entrys.getKey()
                                + "\"; filename=\"" + filename + "\"" + LINEND);
                sbFile = sbFile.append("Content-Type: text/plain" + LINEND + LINEND); // 类型根据上传文件做修改
                output.write(sbFile.toString().getBytes());
                output.write(entry.getBytes());
                output.write(LINEND.getBytes());
            }

            // 结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            output.write(end_data);
            output.flush();

            // 得到响应码
            int code = hc.getResponseCode();
            StringBuffer stringBuffer = new StringBuffer();
            if (code == HttpURLConnection.HTTP_OK) {
                String strCurrentLine;
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(hc.getInputStream()));
                while ((strCurrentLine = reader.readLine()) != null) {
                    stringBuffer.append(strCurrentLine).append("\n");
                }
                // System.out.print(stringBuffer.toString());
                reader.close();
            }
            String mResult = stringBuffer.toString();
            return mResult;
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("success", false);
            data.put("status", "failure");
            data.put("error", 1);
            data.put("url", "");
            data.put("message", e.getMessage());
            JSONObject json = JSONObject.fromObject(data);
            return json.toString();
        }
    }

    /**
     * 通过post发送文件
     *
     * @param url
     * @param mMap
     * @param fileMap
     * @return
     */
    public static String postFile(String url, Map<String, String> mMap, Map<String, File> fileMap) {
        try {
            String BOUNDARY = UUID.randomUUID().toString(); // 分隔符
            String PREFIX = "--", LINEND = "\r\n";
            String MULTIPART_FROM_DATA = "multipart/form-data";

            // 设置HTTP头
            URL requestUrl = new URL(url);
            HttpURLConnection hc = (HttpURLConnection) requestUrl.openConnection();
            hc.setRequestMethod("POST");
            hc.setDoOutput(true);
            hc.setDoInput(true);
            hc.setUseCaches(false);
            hc.setRequestProperty("Connection", "Keep-alive");
            hc.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

            StringBuffer sb = new StringBuffer();
            // 拼接参数
            for (Map.Entry<String, String> entry : mMap.entrySet()) {
                sb = sb.append(PREFIX);
                sb = sb.append(BOUNDARY);
                sb = sb.append(LINEND);
                sb =
                        sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\""
                                + LINEND + LINEND);
                sb = sb.append(entry.getValue());
                sb = sb.append(LINEND);
            }
            OutputStream output = hc.getOutputStream();
            byte[] data = sb.toString().getBytes();
            output.write(data);

            // 拼接文件
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                StringBuilder sbFile = new StringBuilder();
                String path = entry.getValue().getAbsolutePath();
                // 文件名进行转码，防止中文乱码
                String filename =
                        URLEncoder.encode(path.substring(path.lastIndexOf("\\") + 1), "UTF-8");
                sbFile = sbFile.append(PREFIX);
                sbFile = sbFile.append(BOUNDARY);
                sbFile = sbFile.append(LINEND);
                sbFile =
                        sbFile.append("Content-Disposition: form-data; name=\"" + entry.getKey()
                                + "\"; filename=\"" + filename + "\"" + LINEND);
                sbFile = sbFile.append("Content-Type: text/plain" + LINEND + LINEND); // 类型根据上传文件做修改
                output.write(sbFile.toString().getBytes());
                output.write(FileToBytes(entry.getValue()));
                output.write(LINEND.getBytes());
            }

            // 结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            output.write(end_data);
            output.flush();

            // 得到响应码
            int code = hc.getResponseCode();
            StringBuffer stringBuffer = new StringBuffer();
            if (code == HttpURLConnection.HTTP_OK) {
                try {
                    String strCurrentLine;
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(hc.getInputStream()));
                    while ((strCurrentLine = reader.readLine()) != null) {
                        stringBuffer.append(strCurrentLine).append("\n");
                    }
                    System.out.print(stringBuffer.toString());
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String mResult = stringBuffer.toString();
            return mResult;
        } catch (Exception e) {
            e.printStackTrace();
            return "upload error";
        }
    }

    /**
     * 处理文件
     *
     * @param f
     * @return
     */
    private static byte[] FileToBytes(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            while ((n = stream.read(b)) != -1) {
                out.write(b, 0, n);
            }

            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }
}
