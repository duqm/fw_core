package net.sanmuyao.core.util;


public class PasswdUtil {



	/**
	 * 加密
	 * @param inputString
	 * @return
	 */
//	public static String createPassword(String inputString) {
//		return md5(inputString);
//	}
	

	/**
	 * 加密
	 * @param inputString
	 * @return
	 */
	public static String createPassword(String inputString, String attach) {
		return MD5Util.md5(inputString + "-" + attach);
	}

	/**
	 * 验证输入的密码是否正确
	 * 
	 * @param password
	 *            真正的密码（加密后的真密码）
	 * @param inputString
	 *            输入的字符串
	 * @return 验证结果，boolean类型
	 */
	public static boolean authenticatePassword(String password, String inputString, String attach) {
		String inputpwd = MD5Util.md5(inputString + "-" + attach);
		if (password.equalsIgnoreCase(inputpwd)) {
			return true;
		} else {
			return false;
		}
	}
}
