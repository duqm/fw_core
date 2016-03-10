package net.sanmuyao.core.db;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用于封装查询条件的类
 * @author  杜庆明
 *
 */
public class QueryMap {



	public static final String field = "field";
	public static final String opt = "opt";
	public static final String value = "value";
	public static final String rel = "rel";

	public static final String OPT_EQUAL = "=";
	public static final String OPT_NE = "!=";
	public static final String OPT_LIKE = "like";
	public static final String OPT_LT = "<";
	public static final String OPT_GT = ">";
	public static final String OPT_LTE = "<=";
	public static final String OPT_GTE = ">=";
	/**
	 * in 条件，值可以是字符串，数组，List
	 */
	public static final String OPT_IN = "in";
	public static final String OPT_NOT_IN = "not in";
	public static final String OPT_IS = "is";
	public static final String OPT_IS_NOT = "is not";

	public static final String REL_OR = "or";
	public static final String REL_AND = "and";
	// 组合查询
	public static final String query_group = "query_group";

	private Map<String, Map<String, Object>> queryMap = null;

	public Map<String, Map<String, Object>> getQueryMap() {
		if(queryMap == null) {
			queryMap = new HashMap<String, Map<String, Object>>();
		}
		return queryMap;
	}

	/**
	 * 构造一个带条件的QueryMap方法
	 */
	public QueryMap() {
		queryMap = new LinkedHashMap<String, Map<String, Object>>();
	}

	/**
	 * 构造一个带条件的QueryMap方法
	 * @param field 要查询的字段
	 * @param opt 操作
	 * @param value 值
	 * @return
	 */
	public QueryMap(String field, String opt, Object value) {
		queryMap = new LinkedHashMap<String, Map<String, Object>>();
		Map<String, Object> query = new HashMap<String, Object>();
		query.put(QueryMap.field,  field);
		query.put(QueryMap.opt,  opt);
		query.put(QueryMap.value,  value);
		queryMap.put("key"+queryMap.size(), query);
	}

	/**
	 * = 条件
	 * @param field
	 * @param value
	 */
	public void put(String field, Object value) {
		put(field, QueryMap.OPT_EQUAL, value);
	}

	/**
	 * 添加一个条件
	 * @param field
	 * @param opt =、 like、 >、 <、 <>、is
	 * @param value
	 */
	public void put(String field, String opt, Object value) {
		Map<String, Object> query = new HashMap<String, Object>();
		query.put(QueryMap.field,  field);
		query.put(QueryMap.opt,  opt);
		query.put(QueryMap.value,  value);
		queryMap.put("key"+queryMap.size(), query);
	}

	/**
	 * 添加一个条件
	 * @param field
	 * @param opt =、 like、 >、 <、 <>、is
	 * @param value
	 * @param rel 此条件与其他条件的关系 and 或 or，默认为 and
	 */
	public void put(String field, String opt, Object value, String rel) {
		Map<String, Object> query = new HashMap<String, Object>();
		query.put(QueryMap.field,  field);
		query.put(QueryMap.opt,  opt);
		query.put(QueryMap.value,  value);
		query.put(QueryMap.rel,  rel);
		queryMap.put("key"+queryMap.size(), query);
	}

	/**
	 * 返回一个QueryMap实例
	 * @return
	 */
	public static QueryMap getInstance() {
		return new QueryMap();
	}

	/**
	 * 返回一个QueryMap实例
	 * @param field
	 * @param opt
	 * @param value
	 * @return
	 */
	public static QueryMap getInstance(String field, String opt, Object value) {
		QueryMap qm = new QueryMap();
		qm.put(field, opt, value);
		return qm;
	}
}
