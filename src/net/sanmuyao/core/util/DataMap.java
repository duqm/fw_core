package net.sanmuyao.core.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据封装类
 * @author duqingming@qq.com
 *
 */
public class DataMap extends HashMap<String, Object> {

	private static final long serialVersionUID = -6312792986374749377L;

	public static DataMap getInstance() {
		DataMap dataMap = new DataMap();
		return dataMap;
	}

	/**
	 * 生成一个有默认数据的实例
	 * @param key
	 * @param value
	 * @return
	 */
	public static DataMap getInstance(String key, Object value) {
		DataMap dataMap = new DataMap();
		dataMap.put(key, value);
		return dataMap;
	}

	/**
	 *
	 * @param member
	 * @return
	 */
	public static DataMap getInstance(Map<String, Object> data) {
		DataMap dataMap = new DataMap();
		dataMap.putAll(data);
		return dataMap;
	}

	public DataMap() {
		super();
	}

	public DataMap(Map<? extends String, ? extends Object> map) {
		super(map);
	}

	/**
	 * 获取String值
	 *
	 * @param field
	 * @return
	 */
	public String getString(String field) {
		return ConverterUtil.toString(get(field));
	}
	
	/**
	 * 取时间类型
	 * @param field
	 * @return
	 */
	public Date getDate(String field) {
		Object val = get(field);
		if(val instanceof Date) {
			return (Date)val;
		}
		return null;
	}

	/**
	 * 获取Boolean值
	 *
	 * @param field
	 * @return
	 */
	public boolean getBool(String field) {
		return ConverterUtil.toBool(get(field));
	}

	/**
	 * 获取Int值
	 *
	 * @param field
	 * @return
	 */
	public int getInt(String field) {
		return ConverterUtil.toInt(get(field));
	}

	/**
	 * 获取Float值
	 *
	 * @param field
	 * @return
	 */
	public Float getFloat(String field) {
		return ConverterUtil.toFloat(get(field));
	}

	/**
	 * 获取Double值
	 *
	 * @param field
	 * @return
	 */
	public double getDouble(String field) {
		return ConverterUtil.toDouble(get(field));
	}

	/**
	 * 获取Long值
	 *
	 * @param field
	 * @return
	 */
	public long getLong(String field) {
		return ConverterUtil.toLong(get(field));
	}
}
