package net.sanmuyao.base;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sanmuyao.core.db.QueryMap;
import net.sanmuyao.core.util.ConverterUtil;
import net.sanmuyao.core.util.DataMap;
import net.sanmuyao.core.util.DateUtil;
import net.sanmuyao.core.util.Id;
import net.sanmuyao.core.util.ResultEntity;
import net.sanmuyao.core.util.ValidateUtil;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Title: BaseService.java  
 * @Package net.sanmuyao.base
 * @Description: 业务逻辑类基类，理论上所有业务层的类都需要继承此类 
 * @author 杜庆明  duqingming@qq.com
 * @date 2015年1月29日 上午11:41:11
 * @version V1.0    
 */
public abstract class BaseService {

	/**
	 * 基本的数据库操作类
	 */
	@Resource(name = "commonRepository")
	protected BaseRepository baseRepository;

	
	/*****************************************************************************
	 *     抽象方法 start
	 *****************************************************************************/
	
	/**
	 * 
	 * @param dataMap
	 * @return
	 * @throws Exception
	 */
	public abstract boolean add(DataMap dataMap) throws Exception;
	/**
	 * 删除数据
	 * @param dataMap
	 * @return
	 * @throws Exception
	 */
	public abstract int del(String id) throws Exception;
	/**
	 * 更新数据
	 * @param dataMap 要更新的数据，格式<要更新的字段, 要更新的值>
	 * @param id 要更新数据的id
	 * @return
	 * @throws Exception
	 */
	public abstract int update(DataMap dataMap, String id) throws Exception;
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public abstract Map<String, Object> getMap(String id) throws Exception;

	/**
	 * 根据查询条件取数据条数
	 * @param queryMap
	 * @return
	 * @throws Exception
	 */
	public abstract Long getListCount(QueryMap queryMap) throws Exception;
	/**
	 * 取列表数据
	 * @param queryMap 查询条件
	 * @param sidx 排序字段
	 * @param sord 排序方式  desc ascC
	 * @param start 
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public abstract List<Map<String, Object>> getList(QueryMap queryMap, String sidx, String sord, int start, int limit) throws Exception;
	
	/*****************************************************************************
	 *     抽象方法 end 
	 *****************************************************************************/
	
	/**
	 * 返回执行结果
	 * @param success
	 * @param msg
	 * @return
	 */
	protected ResultEntity result(boolean success, String msg) {
		ResultEntity resultEntity = new ResultEntity();
		resultEntity.setSuccess(success);
		resultEntity.setMsg(msg);
		return resultEntity;
	}

	/**
	 * 根据类型取自增的代码
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String getNextNum(String key)  {
		try {
			return getNextNum(key, -1);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据类型取自增的代码
	 * @param key
	 * @param longth 长度，如果取到的数字的长度不足，则补0，如果不对长度做要求请写-1
	 * @return
	 * @throws Exception
	 */
	// @Transactional(readOnly = false, rollbackFor = DataAccessException.class)
	public String getNextNum(String key, int length) throws Exception {
		int code = 1;
		String tableName = "sys_code";
		// 查询条件
		QueryMap queryMap = QueryMap.getInstance("id", "=", key);
		List<Map<String, Object>> list = baseRepository.getList(tableName, queryMap);
		if (ValidateUtil.isListNull(list)) {
			DataMap dataMap = DataMap.getInstance();
			dataMap.put("id", key);
			dataMap.put("code", code);
			baseRepository.add(tableName, dataMap);
		} else {
			code = ConverterUtil.toInt(list.get(0).get("code")) + 1;
			DataMap dataMap = DataMap.getInstance();
			dataMap.put("code", code);
			baseRepository.update(tableName, "id", dataMap, key);
		}
		String str = String.valueOf(code);
		if (length > 0) {
			int step = length - str.length();
			while (step-- > 0) {
				str = "0" + str;
			}
		}
		return str;
	}

	/**
	 * 写系统日志
	 * @param system 系统名称：weixin、bms
	 * @param module 模块名称：公证申请、公证审批
	 * @param operate 进行的操作：add、update、delete、view、pay
	 * @param log_type 日志类型：
	 * @param log_content 日志内容
	 */
	public void log(String system, String module, String operate, String log_content) {
		log(system, module, operate, "", "", "", "", "", "", log_content);
	}
	
	/**
	 * 写系统日志
	 * @param system 系统名称：weixin、bms
	 * @param module 模块名称：公证申请、公证审批
	 * @param operate 进行的操作：add、update、delete、view、pay
	 * @param log_type 日志类型：
	 * @param log_content 日志内容
	 */
	public void log(String system, String module, String operate, String log_type, String log_content) {
		log(system, module, operate, log_type, "", "", "", "", "", log_content);
	}

	/**
	 * 写系统日志
	 * @param system 系统名称
	 * @param module 模块名称
	 * @param operate 进行的操作
	 * @param log_type 日志类型
	 * @param org_id 机构id
	 * @param org_name 机构名称
	 * @param user_id 用户id
	 * @param user_name 用户名称
	 * @param data_id 业务数据id
	 * @param log_content 日志内容
	 */
	public void log(String system, String module, String operate, String log_type,
			String org_id, String org_name,
			String user_id, String user_name,
			String data_id, String log_content) {
		DataMap dataMap = new DataMap();
		dataMap.put("id", Id.getId());
		dataMap.put("data_id", data_id);  // 业务数据id	data_id	varchar(32)
		dataMap.put("log_content", log_content); // 日志内容	log_content	varchar(512)
		dataMap.put("org_id", org_id);//org_id
		dataMap.put("org_name", org_name);//org_name
		//dataMap.put("dept_id", dept_id);//dept_id	dept_id	varchar(32)
		dataMap.put("user_id", user_id);//user_id
		dataMap.put("user_name", user_name);//user_name	member_name	varchar(32)
		dataMap.put("system", system);//系统	system	varchar(32)
		dataMap.put("module", module);//业务模块	module	varchar(128)
		dataMap.put("operate", operate);//操作	operate	varchar(32)
		dataMap.put("log_type", log_type);//日志类型	log_type	varchar(32)
		dataMap.put("create_dt", DateUtil.getDate());   // 创建时间	create_dt	datetime
				
		try {
			baseRepository.add("sys_log", dataMap);
		} catch (Exception e) {
			System.err.println("#################### net.sanmuyao.m.service.log()" + e.getLocalizedMessage());
		}
	}

//
//	/**
//	 * 发送消息
//	 * @param shop_id
//	 * @param title
//	 * @param msg
//	 */
//	public void sentMsg(String shop_id, String title, String msg) {
//		sentMsg(shop_id, null, null, title, msg);
//	}
//	/**
//	 * 发送消息
//	 * @param shop_id
//	 * @param send_id
//	 * @param receive_id
//	 * @param title
//	 * @param msg
//	 */
//	public void sentMsg(String shop_id, String send_id, String receive_id, String title, String msg) {
//		Date dt = new Date();
//		DataMap dataMap = DataMap.getInstance();
//		dataMap.put("id", Id.getId());
//		if(ValidateUtil.isNotNull(send_id)) dataMap.put("send_shop_id", shop_id);		// 发送店铺	send_shop_id	varchar(32)
//		dataMap.put("send_user_id", send_id);		// 发送人	send_user_id	varchar(32)
//		dataMap.put("send_dt", dt);		// 发送时间	send_dt	datetime
//		dataMap.put("receive_shop_id", shop_id);		// 接收店铺	receive_shop_id	varchar(64)
//		if(ValidateUtil.isNotNull(receive_id)) dataMap.put("receive_user_id", receive_id);		// 接收人	receive_user_id	varchar(64)
//		//dataMap.put("receive_dt", );		// 接收时间	receive_dt	datetime
//		dataMap.put("title", title);		// 标题	title	varchar(32)
//		dataMap.put("content", msg);		// 内容	content	varchar(512)
//		dataMap.put("msg_level", 1);		// 级别	1:立刻发送; 2: 允许延迟; 3:最慢级别
//		dataMap.put("msg_type", "sys");		// 发送人类型	msg_type	varchar(16)
//		dataMap.put("msg_status", 1);		// 发送状态	-1:草稿; 1:发送; 2:已接收;
//		dataMap.put("create_dt", dt);		// 创建时间	create_dt	datetime
//		dataMap.put("send_info", "系统消息");		// 发送说明	send_info	varchar(512)
//		dataMap.put("is_del", 1);		// 发送人是否删除	-2:发件人删除-1:收件人删除;1:未删除
//		try {
//			if(baseRepository.add("sys_msg", dataMap));
//		} catch(Exception ex) {}
//	}

	/**
	 * 取系统的request对象
	 * @return
	 */
	public  HttpServletRequest getRequest() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attrs.getRequest();
	}


}
