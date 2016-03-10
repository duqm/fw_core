package net.sanmuyao.base;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @Description: 控制器基类，所有控制器都需要直接或间接继承此类
 * @author 杜庆明  duqingming@qq.com
 * @date 2015年1月29日 上午11:40:52
 * @version V1.0    
 */
public abstract class BaseController {
	


	/**
	 * 列表页面初始化
	 * @param request
	 * @param model
	 * @return
	 */
	public abstract String init(HttpServletRequest request, HttpServletResponse response, ModelMap model);
	
    /**
     * 返回分页格式的json数据，支持 easyui-treegrid  easyui-datagrid
     * @param total
     * @param rows
     * @return
     */
	public String returnPaginationJson(long total, List<Map<String, Object>> rows) {
		Map<String, Object> paginationMap = new HashMap<String, Object>();
		paginationMap.put("total", total);
		paginationMap.put("rows", rows);
		return JSON.toJSONString(paginationMap);
	}
	
	/**
	 * 操作成功后返回json格式的数据
	 *
	 * @param msg
	 * @return
	 */
	protected String success(String msg) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("success", true);
		data.put("status", "success");
		data.put("msg", msg);
		return JSONObject.toJSONString(data);

	}

	/**
	 * 操作成功后返回json格式的数据
	 *
	 * @param msg
	 * @return
	 */
	protected String success(String msg, Map<String, Object> data) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("success", true);
		dataMap.put("status", "success");
		dataMap.put("msg", msg);
		if(data != null) dataMap.put("data", data);
		return JSONObject.toJSONString(dataMap);
	}

	/**
	 * 操作成功后返回json格式的数据
	 *
	 * @param msg
	 * @param data
	 * @return
	 */
	protected String success(String msg, String data) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("success", true);
		dataMap.put("status", "success");
		dataMap.put("msg", msg);
		dataMap.put("data", data);
		return JSONObject.toJSONString(dataMap);
	}

	/**
	 * 操作发生错误时执行的操作
	 *
	 * @param msg
	 * @return
	 */
	protected String failure(String msg) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("success", false);
		data.put("status", "failure");
		data.put("msg", msg);
		return JSONObject.toJSONString(data);
	}

	/**
	 * 操作发生错误时执行的操作
	 *
	 * @param msg
	 * @param extMap
	 * @return
	 */
	protected String failure(String msg, Map<String, Object> extMap) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("success", false);
		data.put("status", "failure");
		data.put("msg", msg);
		if(extMap!=null) data.put("data", extMap);
		return JSONObject.toJSONString(data);
	}

	/**
	 * 操作发生异常时执行的操作
	 *
	 * @param msg
	 * @return
	 */
	protected String error(Exception e) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("success", false);
		data.put("status", "error");
		data.put("msg", e.getLocalizedMessage());
		return JSONObject.toJSONString(data);
	}

	/**
	 * 发生异常
	 *
	 * @param e
	 * @param extMap
	 * @return
	 */
	protected String error(Exception e, Map<String, Object> extMap) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("success", false);
		data.put("status", "error");
		data.put("msg", e.getMessage());
		data.put("data", extMap);
		return JSONObject.toJSONString(data);
	}

	/**
	 * 
	 * getUploadServer:  <br/>
	 * 取文件上传请求地址，需要在 web.xml 中设置如：<br/>
	 * 	<context-param>
	 * 	    <param-name>upload_server</param-name>
	 * 	    <param-value>http://192.168.1.101:8080/upload_server/</param-value>
	 * 	</context-param>
	 * @author duqingming@126.com
	 * @param request
	 * @return
	 */
	public String getSaveFileRootPath(HttpServletRequest request) {
		String upload_server = request.getSession().getServletContext().getInitParameter("save_file_root_path");
		return upload_server;
	}
	

	/**
	 * 取根路径
	 * @param request
	 * @return
	 */
	protected String getBasePath(HttpServletRequest request) {
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() + request.getContextPath();
		return basePath;
	}

	/**
	 * 
	 * getFileServer:  <br/>
	 * 文件服务器地址，取文件上传请求地址，需要在 web.xml 中设置如：<br/>
	 * 	<context-param>
	 * 	    <param-name>file_server</param-name>
	 * 	    <param-value>http://192.168.1.101:8080/file_server/</param-value>
	 * 	</context-param>
	 * @author duqingming@126.com
	 * @param request
	 * @return
	 */
	public String getFileServer(HttpServletRequest request) {
		String file_server = request.getSession().getServletContext().getInitParameter("file_server");
		return file_server;
	}
	
	/**
	 * 打印出request中的参数
	 *
	 * @param request
	 */
	public void printRequest(HttpServletRequest request) {
		Enumeration<String> keys = request.getParameterNames();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			System.out.println("########## " + key + " = "+ request.getParameter(key));
		}
	}

}
