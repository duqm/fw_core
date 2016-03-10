package net.sanmuyao.servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;

/**
 * 前端控制器
 * @author 杜庆明
 * @version v1.0
 */
public class MyDispatcherServlet extends DispatcherServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String encoding = "UTF8";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding(encoding);
		response.setCharacterEncoding(encoding);
		super.doService(request, response);
	}
}
