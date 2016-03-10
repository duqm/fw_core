package net.sanmuyao.core.util;

/**
 * 返回参数
 * @author 杜庆明
 * 2015年4月7日 15:09:32
 */
public class ResultEntity {

	/**
	 * 执行结果
	 */
	private boolean success = false;

	/**
	 * 返回消息
	 */	
	private String msg = "";
	
	/**
	 * 返回数据
	 */
	private DataMap dataMap = null;
	
	/**
	 * 创建返回数据信息
	 */
	public ResultEntity() {
		this.success = false;
		this.msg = "";
		this.dataMap = DataMap.getInstance();
	}
	
	/**
	 * 创建返回数据信息
	 * @param success
	 */
	public ResultEntity(boolean success) {
		this.success = success;
		this.msg = "";
		this.dataMap = DataMap.getInstance();
	}
	
	/**
	 * 创建返回数据信息
	 * @param success
	 * @param msg
	 */
	public ResultEntity(boolean success, String msg) {
		this.success = success;
		this.msg = msg;
		this.dataMap = DataMap.getInstance();
	}
	
	
	/**
	 * 创建返回数据信息
	 * @param success
	 * @param msg
	 * @param dataMap
	 */
	public ResultEntity(boolean success, String msg, DataMap dataMap) {
		this.success = success;
		this.msg = msg;
		this.dataMap = dataMap;
	}
	
	public static ResultEntity getInstance() {
		return new ResultEntity();
	}	
	public static ResultEntity getInstance(boolean success) {
		return new ResultEntity(success);
	}
	public static ResultEntity getInstance(boolean success, String msg) {
		return new ResultEntity(success, msg);
	}
	public static ResultEntity getInstance(boolean success, String msg, DataMap dataMap) {
		return new ResultEntity(success, msg, dataMap);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public DataMap getDataMap() {
		return dataMap;
	}

	public void setDataMap(DataMap dataMap) {
		this.dataMap = dataMap;
	}
	
	
}
