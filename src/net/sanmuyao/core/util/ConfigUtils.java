package net.sanmuyao.core.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Config Utils: global HttpServletRequest, server config, etc.
 *
 * @author subo
 * @date 2015年2月9日
 */
public class ConfigUtils {

    /**
     * web.xml <listener> <listener-class>
     * org.springframework.web.context.request.RequestContextListener </listener-class> </listener>
     *
     * @return
     */
    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs.getRequest();
    }

    public static ServletContext getServletContext() {
        HttpServletRequest request = getHttpServletRequest();
        if (request != null) {
            return request.getSession().getServletContext();
        }
        return null;
    }

    /**
     * 取上传服务器路径
     *
     * @return
     */
    public static String getUploadServer() {
        String upload_server = getServletContext().getInitParameter("upload_server");
        return upload_server;
    }

    /**
     * 取文件服务器路径
     *
     * @return
     */
    public static String getFileServer() {
        String file_server = getServletContext().getInitParameter("file_server");
        return file_server;
    }

    /**
     * 二维码服务器地址
     *
     * @return
     */
    public static String getQRServer() {
        String file_server = getServletContext().getInitParameter("qr_server");
        return file_server;
    }

    public static String getMarketServer() {
        return getRootPath();
    }

    /**
     * 取生成二维码的接口地址
     *
     * @param request
     * @return
     */
    public static String getQRCodePath() {
        return getUploadServer() + "/qrcode/create.htm";
    }

    /**
     * 取生成二维码的接口地址
     *
     * @param request
     * @return
     */
    public static String getQRCodeCardPath() {
        return getUploadServer() + "/qrcode/createcard.htm";
    }

    /**
     * 取当前服务器路径
     *
     * @return
     */
    public static String getRootPath() {
        HttpServletRequest request = getHttpServletRequest();

        String port = "";
        if (request.getServerPort() != 80) {
            port = ":" + request.getServerPort();
        }
        String basePath =
                request.getScheme() + "://" + request.getServerName() + port
                        + request.getContextPath();
        return basePath;
    }

}
