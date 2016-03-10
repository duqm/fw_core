package net.sanmuyao.core.redis;

/**
 * 保存在redis中内容的key值
 * @author 杜庆明
 *
 */
public enum RedisKey {


	/**
	 * 需要发送的邮箱列表
	 */
	mail_list("bmy_mail_list", "需要发送的邮箱"),

	/**
	 * 需要发送的短信的列表
	 */
	sms_list("bmy_sms_list", "需要发送的短信的列表"),

	/**
	 * 发送成功的短信列表
	 */
	sms_sendsuccess_list("bmy_sms_sendsuccess_list", "需要发送的短信的列表");




	private String code;
	private String desc;

	private RedisKey(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		return code;
	}
}
