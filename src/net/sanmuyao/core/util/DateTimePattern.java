package net.sanmuyao.core.util;

/**
 * 日期时间格式化字符串
 */
public enum DateTimePattern {

	/**
	 * HH:mm
	 */
	hoursMinute("HH:mm", "HH:mm", ""),
	/**
	 * MM月dd日
	 */
	monthDay("MM月dd日", "MM月dd日", ""),
	/**
	 * HH:mm:ss
	 */
	shortTimePattern("HH:mm:ss", "HH:mm:ss", ""),
	/**
	 * HH时mm分ss秒
	 */
	longTimePattern("HH时mm分ss秒", "HH时mm分ss秒", ""),
	/**
	 * yyyy-MM-dd
	 */
	shortDatePattern("yyyy-MM-dd", "yyyy-MM-dd", ""),
	/**
	 * MM/dd/yyyy
	 */
	datePattern("MM/dd/yyyy", "MM/dd/yyyy", ""),
	/**
	 * MM/dd/yyyy HH:mm:ss
	 */
	datetimePattern("MM/dd/yyyy HH:mm:ss", "MM/dd/yyyy HH:mm:ss", ""),
	/**
	 * yyyy年MM月dd日
	 */
	longDatePattern("yyyy年MM月dd日", "yyyy年MM月dd日", ""),
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	shortDateTimePattern ("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", ""),
	/**
	 * yyyy年MM月dd日 HH时mm分ss秒
	 */
	longDateTimePattern ("yyyy年MM月dd日 HH时mm分ss秒", "yyyy年MM月dd日 HH时mm分ss秒", "");
	
	private String code;
	private String name;
	private String remark;	

	private DateTimePattern(String code, String name, String remark) {
		this.code = code;
		this.name = name;
		this.remark = remark;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}	
	
}
