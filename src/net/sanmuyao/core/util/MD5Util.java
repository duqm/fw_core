package net.sanmuyao.core.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * MD5 Utility Class
 * 取自微信支付的demo
 */
public class MD5Util {
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7",
        "8", "9", "a", "b", "c", "d", "e", "f"};
	
	/**
	 * 转换字节数组为16进制字串
	 * @param b 字节数组
	 * @return 16进制字串
	 */
	protected static String byteArrayToHexString(byte[] b) {
	    StringBuilder resultSb = new StringBuilder();
	    for (byte aB : b) {
	        resultSb.append(byteToHexString(aB));
	    }
	    return resultSb.toString();
	}
	
	/**
	 * 转换byte到16进制
	 * @param b 要转换的byte
	 * @return 16进制格式
	 */
	private static String byteToHexString(byte b) {
	    int n = b;
	    if (n < 0) {
	        n = 256 + n;
	    }
	    int d1 = n / 16;
	    int d2 = n % 16;
	    return hexDigits[d1] + hexDigits[d2];
	}
	
	/**
	 * MD5编码
	 * @param origin 原始字符串
	 * @return 经过MD5加密之后的结果
	 */
	public static String md5(String origin) {
	    String resultString = null;
	    try {
	        resultString = origin;
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(resultString.getBytes("UTF-8"));
	        resultString = byteArrayToHexString(md.digest());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return resultString;
	}
	
	/**
	 * 对map进行md5加密
	 * @param map
	 * @param key
	 * @return
	 */
    public static String getSign(Map<String,Object> map, String key){
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(entry.getValue()!=""){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + key;
        //Util.log("Sign Before MD5:" + result);
        result = md5(result).toUpperCase();
        //Util.log("Sign Result:" + result);
        return result;
    }
}
