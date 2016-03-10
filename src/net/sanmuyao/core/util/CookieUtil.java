package net.sanmuyao.core.util;

import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie utilities
 *
 * @date 2014-06-26
 */
public class CookieUtil {


	/**
	 * Default expire time in seconds
	 */
	public static final int DEFAULT_EXPIRY = 30 * 24 * 60 * 60;

	/**
	 * Default cookie path
	 */
	private static final String DEFAULT_PATH = "/";


	/**
	 * Set default Http Cookie
	 *
	 * @param response
	 * @param name
	 * @param value
	 */
	public static void setCookie(HttpServletResponse response, String name, String value) {
		setCookie(response, name, value, DEFAULT_PATH, DEFAULT_EXPIRY);
	}

	/**
	 * Set Http Cookie
	 *
	 * @param response
	 * @param name
	 * @param value
	 * @param path  The cookie path, default /
	 * @param expiry The cookie expire time in seconds
	 */
	private static void setCookie(HttpServletResponse response, String name, String value, String path, int expiry) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath(path);
		// 0: delete immediately, -1: delete when session closed
		cookie.setMaxAge(expiry);
		//cookie.setDomain("");

		response.addCookie(cookie);
	}

	/**
	 * Get Http Cookie
	 *
	 * @param request
	 * @param name
	 * @return The cookie value if existing, null otherwise.
	 */
	public static String getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length == 0) return null;

		for (Cookie cookie : cookies) {
			if (cookie.getName().equalsIgnoreCase(name)) {
				return cookie.getValue();
			}
		}

		return null;
	}

	/**
     * Get Http Cookie
     *
     * @param request
     * @param name
     * @param decode
     * @return The cookie value if existing, null otherwise.
     */
    public static String getCookie(HttpServletRequest request, String name, boolean decode) {
        String cookie = getCookie(request, name);
        if (!decode || cookie == null) return cookie;

        try {
            return URLDecoder.decode(cookie, "UTF-8");
        } catch (Exception e) {
            // do nothing
        }
        return null;
    }

	/**
	 * Remove the specified cookie.
	 *
	 * @param response
	 * @param name
	 */
	public static void removeCookie(HttpServletResponse response, String name) {
		setCookie(response, name, null, DEFAULT_PATH, 0);
	}
}
