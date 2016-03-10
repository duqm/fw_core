package net.sanmuyao.core.interceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sanmuyao.core.util.BaseGlobal;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 拦截器
 * @author dqm
 *
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 会员登录页面
	 */
	private String loginPage = null;
	
	/**
	 * 用户登录界面
	 */
	private String uloginPage = null;

	/**
	 * 忽略的页面
	 */
	private List<String> ignoreList = new ArrayList<String>();
	
	/**
	 * 需要拦截的路径
	 */
	private List<String> interceptorUrl = null;


	/**
	 * 拦截
	 */
	@SuppressWarnings("unchecked")
	public boolean preHandle(HttpServletRequest request, HttpServletResponse resp, Object handler) {
		String url = request.getRequestURL().toString();		// 请求地址
		// 是否忽略
		for (String str : ignoreList) {
			if (url.contains(str)) {
				return true;
			}
		}
		// 验证是否过滤
		Enumeration<String> paras = request.getParameterNames();		// 请求参数		
		boolean isInterceptor = false; 	// 是否需要拦截
		for(String str : interceptorUrl) {
			if (url.contains(str)) {
				isInterceptor = true;
			}
		}
		if (isInterceptor) {

			HttpSession session = request.getSession();
			Map<String, Object> userMap = (Map<String, Object>) session.getAttribute(BaseGlobal.LOGIN_USER_SESSION);

			if (userMap == null) {
				// 保存被拦截的地址
				String parasStr = "";
				String key = null;
				while(paras.hasMoreElements()) {
					key = paras.nextElement().toString();
					if(parasStr.equals("")) {
						parasStr = "?";
					} else {
						parasStr += "&";
					}
					parasStr += key + "=" + request.getParameter(key);
				}
				// 跳转到登录页面
				request.getSession().setAttribute("interceptor_url", url+parasStr);
				
				// debug
				System.out.println("========== LoginInterceptor ==========");
				System.out.println("SESSION ID: "+session.getId());
				System.out.println("Request Interceptor: " + url+parasStr);
				
				redirect(request, resp, loginPage);  // 跳转到登录页面
				return false;
			}
		}
		return true;
	}

	/**
	 * 页面跳转
	 */
	private void redirect(HttpServletRequest request, HttpServletResponse resp, String url) {
		try {
			resp.sendRedirect(request.getContextPath() + url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getLoginPage() {
		return loginPage;
	}
	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	public String getUloginPage() {
		return uloginPage;
	}

	public void setUloginPage(String uloginPage) {
		this.uloginPage = uloginPage;
	}
	public List<String> getIgnoreList() {
		return ignoreList;
	}

	public void setIgnoreList(List<String> ignoreList) {
		this.ignoreList = ignoreList;
	}

	public List<String> getInterceptorUrl() {
		return interceptorUrl;
	}

	public void setInterceptorUrl(List<String> interceptorUrl) {
		this.interceptorUrl = interceptorUrl;
	}

	
}
