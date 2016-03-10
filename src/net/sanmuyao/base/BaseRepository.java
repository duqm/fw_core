package net.sanmuyao.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sanmuyao.core.db.LogDbHelper;
import net.sanmuyao.core.db.MsgDbHelper;
import net.sanmuyao.core.db.QueryMap;
import net.sanmuyao.core.db.ReadDbHelper;
import net.sanmuyao.core.db.WriteDbHelper;
import net.sanmuyao.core.util.DataMap;
import net.sanmuyao.core.util.ValidateUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

import com.sun.istack.internal.NotNull;

/**
 * @author 杜庆明
*  @Description: 数据库访问层基础类
*  @date 2014年6月5日20时1分
*  @version V1.0
 */
public abstract class BaseRepository {

	/**
	 * 日志工具类
	 */
	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * 读数据调用
	 */
	@NotNull
	@Autowired
	protected ReadDbHelper readDbHelper = null;

	/**
	 * 写数据调用
	 */
	@NotNull
	@Autowired
	protected WriteDbHelper writeDbHelper = null;
	
	/**
	 * 写日志时调用
	 */
	@NotNull
	@Autowired
	protected LogDbHelper logDbHelper = null;
	
	/**
	 * msg
	 */
	@NotNull
	@Autowired
	protected MsgDbHelper msgDbHelper = null;
	
//	@NotNull
//	@Autowired
//	protected MongodbHelper mongodbHelper = null;
	
	


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
	 * 取列表数据，按默认排序，取所有数据
	 * 建议添加最大数据限制条件，防止查询数据过大，一次性取出数据量最大应不超过2000条，如果数据量大于50条以上请考虑用其他方法
	 * @param queryMap
	 * @return
	 * @throws Exception
	 */
	public abstract List<Map<String, Object>> getList(QueryMap queryMap) throws Exception;
	/**
	 * 取列表数据，带排序条件
	 * 建议添加最大数据限制条件，防止查询数据过大，一次性取出数据量最大应不超过2000条，如果数据量大于50条以上请考虑用其他方法
	 * @param queryMap
	 * @param sidx
	 * @param sord
	 * @return
	 * @throws Exception
	 */
	public abstract List<Map<String, Object>> getList(QueryMap queryMap, String sidx, String sord) throws Exception;
	
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
	 * @param sord 排序方式  desc asc
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
	 * 取表的记录条数
	 * @param tableName 表名
	 * @return
	 */
	protected int getTotalCount(String tableName) {
		try {
			return readDbHelper.getTotal("select count(*) from "+tableName);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	/**
	 *  取表的记录条数
	 * @param tableName 表名
	 * @param queryMap 条件
	 * @return
	 * @throws Exception
	 */
	protected Long getListCount(String tableName, QueryMap queryMap) throws Exception {
		LinkedList<Object> argList = new LinkedList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*)  from " + tableName);
		// 查询条件
		String where = joinWhere(queryMap, argList);
		if(where!=null && where.length()>0) {
			sql.append(" where " + where);
		}
		return readDbHelper.getTotal(sql, argList);
	}

	/**
	 * 取列表数据，支持过滤条件
	 * @param tableName
	 * @param sort
	 * @param dir
	 * @return
	 * @throws Exception
	 */
	protected List<Map<String, Object>> getList(String tableName, QueryMap queryMap) throws Exception {
		LinkedList<Object> argList = new LinkedList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select *  from " + tableName);
		// 查询条件
		String where = joinWhere(queryMap, argList);
		if(where!=null && where.length()>0) {
			sql.append(" where " + where);
		}
		return readDbHelper.getList(sql, argList);
	}
	
	/**
	 * 取列表数据，支持过滤条件
	 * @param tableName 要查询的表名，支持 left join 将两个表拼接到一起
	 * @param columns 要返回的列
	 * @param queryMap
	 * @return
	 * @throws Exception
	 */
	protected List<Map<String, Object>> getList(String tableName, String columns, QueryMap queryMap) throws Exception {
		LinkedList<Object> argList = new LinkedList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select *  from " + tableName);
		// 查询条件
		String where = joinWhere(queryMap, argList);
		if(where!=null && where.length()>0) {
			sql.append(" where " + where);
		}
		return readDbHelper.getList(sql, argList);
	}

	/**
	 * 取列表数据，支持排序
	 * @param tableName 表名
	 * @param sort 排序字段
	 * @param dir 排序方式 desc asc
	 * @return
	 * @throws Exception
	 */
	protected List<Map<String, Object>> getList(String tableName, String sidx, String sord) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(tableName);
		if(ValidateUtil.isNotNull(sidx)) {
			sql.append(" order by ").append(sidx).append(" ").append(sord);
		}
		List<Map<String, Object>> list = readDbHelper.getList(sql.toString());
		return list;
	}

	/**
	 *  取列表数据，支持条件查询，排序
	 * @param tableName 要取的数据的表名
	 * @param searchList：取数据的过滤条件。
	 * 一个Map一个条件，最外层Map的key值表示要检索的字段，对应的子Map为条，其中包括操作符 “opt” 和值 “value”，opt 默认值为“=”，value 默认值为null
	 *
	 * @param sort 排序字段
	 * @param dir 排序方式 desc asc
	 * @return
	 * @throws Exception
	 */
	protected List<Map<String, Object>> getList(String tableName, QueryMap queryMap, String sidx, String sord) throws Exception {
		LinkedList<Object> argList = new LinkedList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from " + tableName);
		// 查询条件
		String where = joinWhere(queryMap, argList);
		if(where!=null && where.length()>0) {
			sql.append(" where " + where);
		}
		// 排序
		if(ValidateUtil.isNotNull(sidx)) {
			sql.append(" order by " +sidx);
			if(ValidateUtil.isNotNull(sord)) {
				sql.append(" " +sord);
			}
		}
		return readDbHelper.getList(sql, argList);
	}

	/**
	 * 取列表数据，支持查询，排序，分页
	 * @param tableName 要查询的表名
	 * @param columns 要查询的列
	 * @param searchList 查询条件
	 * @param sort 排序字段
	 * @param dir 排序方式 desc asc
	 * @param start 取数据的起始索引
	 * @param limit 取数据条数
	 * @return
	 * @throws Exception
	 */
	protected List<Map<String, Object>> getList(String tableName, String columns, QueryMap queryMap, String sidx, String sord, int start, int limit) throws Exception {
		StringBuilder sql = new StringBuilder();
		LinkedList<Object> argList = new LinkedList<Object>();
		sql.append("select "+ columns +" from " + tableName);
		// 查询条件
		String where = joinWhere(queryMap, argList);
		if(where!=null && where.length()>0) {
			sql.append(" where " + where);
		}
		// 排序
		if(ValidateUtil.isNotNull(sidx)) {
			sql.append(" order by " +sidx+ " " +sord+ "");
		}
		sql.append(" limit ?, ? ");
		argList.add(start);
		argList.add(limit);

		List<Map<String, Object>> list = readDbHelper.getList(sql, argList);
		return list;
	}

	/**
	 * 取列表数据，支持查询，排序，分页
	 * @param tableName 要查询的表名
	 * @param searchList 查询条件
	 * @param sort 排序字段
	 * @param dir 排序方式 desc asc
	 * @param start 取数据的起始索引
	 * @param limit 取数据条数
	 * @return
	 * @throws Exception
	 */
	protected List<Map<String, Object>> getList(String tableName, QueryMap queryMap, String sidx, String sord, int start, int limit) throws Exception {
		return getList(tableName, "*", queryMap, sidx, sord, start, limit);
	}

//	/**
//	 * 方便取查询条件的map方法
//	 * @param field 要查询的字段
//	 * @param opt 操作
//	 * @param value 值
//	 * @return
//	 */
//	public Map<String, Map<String, Object>> getQueryMaps(String field, String opt, Object value) {
//		Map<String, Map<String, Object>> queryMap = new HashMap<String, Map<String, Object>>();
//		Map<String, Object> query = new HashMap<String, Object>();
//		query.put("opt",  opt);
//		query.put("value",  value);
//		queryMap.put(field, query);
//		return queryMap;
//	}


	/**
	 * 根据QueryMap对象生成sql查询条件语句，条件中的值保存到argList
	 * @param queryMaps
	 * @param argList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected String joinWhere(QueryMap queryMap, LinkedList<Object> argList) {
		StringBuilder sql = new StringBuilder();
		Map<String, Map<String, Object>> queryMaps = queryMap.getQueryMap();
		if (argList == null) argList = new LinkedList<Object>();

		String opt = null;
		Object value = null;
		if(null != queryMaps) {
			int index = 0;
			Iterator<String> keys = queryMaps.keySet().iterator();
			String key = null;
			String field = null;
			Map<String, Object> query = null;
			while(keys.hasNext()) {
				key = keys.next();
				query = queryMaps.get(key);
				field = (String)query.get("field");
				if(index > 0) {
					if(ValidateUtil.isNull(query.get("rel"))) {
						sql.append(" and ");
					} else {
						sql.append(" "+ query.get("rel") +" ");
					}
				}
				opt = query.get("opt").toString().toLowerCase();
				value =query.get("value");
				if(opt.equals("query_group")) {
					// 组合查询
					sql.append("(");
					sql.append(joinWhere((QueryMap)value, argList));
					sql.append(")");
				} else if (opt.equals("like")) {
					if(value != null) {
						sql.append(field).append(" like ?");
						argList.add("%" + value + "%");
					} else {
						sql.append(field).append(" is null");
					}
				} else if (opt.equals("=")) {
					if(value != null) {
						sql.append(field).append("=?");
						argList.add(value);
					} else {
						sql.append(field).append(" is null");
					}
				} else if(opt.equals("in") || opt.equals("not in")) {
					if (value == null) continue;

					Object[] args = null;
					if (value instanceof String) {
						args = StringUtils.split((String) value);
					} else if (value instanceof Object[]) {
						args = (Object[]) value;
					} else if (value instanceof List) {
						args = ((List<Object>) value).toArray(new Object[0]);
					}

					if (args == null || args.length == 0) continue;

					sql.append(field).append(" " + opt).append(" (");
					for (int i = 0; i < args.length; i++) {
						argList.add(args[i] + "");
						sql.append("?");
						if (i < args.length - 1) {
							sql.append(",");
						}
					}
					sql.append(")");
				} else if(opt.equals("is") && value==null) {
					sql.append(field).append(" is null");
				} else if(opt.equals("is not")) {
					sql.append(field).append(" is not null");
				} else {
					sql.append(field).append(opt).append("?");
					argList.add(value);
				}
				index++;
			}
		}
		return sql.toString();
	}
	
	/**
     * 直接执行SQL语句
     * @param sql
     */
	protected void execute(String sql) throws Exception {
		writeDbHelper.execute(sql);
	}

	/**
	 * 执行sql
	 * @param sql
	 * @param args
	 * @throws Exception
	 */
	protected int execute(String sql, List<Object> args) throws Exception {
		return writeDbHelper.execute(sql, args.toArray());
	}
	
	// ==========================================================================
	// 其他添加、删除、更新等操作
	// ==========================================================================

	/**
	 * 添加一条记录，记录的字段根据dataMap中的key取，因此dataMap的key必需和表中的字段保持一致
	 * @param tableName
	 * @param dataMap
	 * @return
	 * @throws Exception
	 */
	protected boolean add(String tableName, Map<String, Object> dataMap) throws Exception {
		boolean result = false;
		List<String> fields = new ArrayList<String>();
		for(String field : dataMap.keySet()){
			fields.add(field);
		}
		try {
			result = writeDbHelper.add(tableName, dataMap, fields);
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	protected Object[] getAddSql(String tableName, Map<String, Object> dataMap) throws Exception {
		return writeDbHelper.getAddSql(tableName, dataMap);
	}

	/**
	 * 批量插入数据
	 * @param tableName
	 * @param data
	 * @return
	 * @throws Exception
	 */
	protected boolean batchInsert(String tableName, List<Map<String, Object>> data) throws Exception {
		try {
			return writeDbHelper.batchInsert(tableName, data);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Use {@link #batchUpdate} instead.
	 *
	 * @param sqlAndParams
	 * @throws Exception
	 */
	protected void batchUpdateWithParam(List<Map<String, Object>> sqlAndParams) throws Exception {
		writeDbHelper.batchUpdateWithParams(sqlAndParams);
	}

    /**
     * Makes prepared sql/param.
     *
     * @param sql
     * @param params
     * @return
     */
	protected static Map<String, Object> makePrepareMap(String sql, Object[] params) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sql", sql);
        map.put("param", params);
        return map;
    }

    /**
     * Makes prepared sql/param.
     *
     * @param builder
     * @param params
     * @return
     */
	protected static Map<String, Object> makePrepareMap(StringBuilder builder, List<Object> params) {
        return makePrepareMap(builder.toString(), params.toArray());
    }

    /**
     * Batch update table with sqls and params.
     *
     * @param tableName
     * @param dataList
     * @param fields
     * @param condList
     * @throws Exception
     */
	protected void batchUpdate(String tableName, List<Map<String, Object>> dataList, List<String> fields, List<Map<String, Object>> condList) throws Exception {
        writeDbHelper.batchUpdate(tableName, dataList, fields, condList);
    }

	/**
	 * 更新记录，通常用于根据记录的id对数据进行更新。
	 * 也可用于只根据一个字段的值来进行的批量更新，如更新所有年龄为18岁的用户的某些信息。
	 * @param tableName 要更新的记录的表名
	 * @param keyFieldName 要更新的记录的id字段的字段名
	 * @param dataMap 要更新的数据
	 * @param id 要更新记录的id
	 * @return
	 * @throws Exception
	 */
	protected int update(String tableName, String keyFieldName, DataMap dataMap, Object id) throws Exception {
    	int result = 0;
		List<String> fields = new ArrayList<String>();
		for(String field : dataMap.keySet()){
			fields.add(field);
		}
		result = writeDbHelper.update(tableName, dataMap, fields, keyFieldName, id);
		return result;
    }

	/**
	 * 更新记录。适用于批量更新或双主键记录的更新。如果是根据记录的唯一id更新更新的操作请使用：update(String tableName, String keyFieldName, DataMap dataMap, Object id)
	 * @param tableName 要更新的表的表名
	 * @param dataMap 要更新的数据
	 * @param queryMap 要更新的数据的条件
	 * @return
	 * @throws Exception
	 */
	protected int update(String tableName, DataMap dataMap, QueryMap queryMap) throws Exception {
		int result = 0;

		// 更新 Fields
		List<String> fields = new ArrayList<String>();
		for(String field : dataMap.keySet()){
			fields.add(field);
		}

		// 更新值
		StringBuilder fieldbuffer = new StringBuilder();
		LinkedList<Object> valueList = new LinkedList<Object>();
		for(String field : fields){
			if(fieldbuffer.length()!=0){
				fieldbuffer.append(",");
			}
			fieldbuffer.append(field+"=?");
			valueList.add(dataMap.get(field));
		}

		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE " + tableName + " SET " + fieldbuffer.toString());

		// 查询条件
		String where = joinWhere(queryMap, valueList);
		if(where!=null && where.length()>0) {
			sql.append(" WHERE " + where);
		}

		result = writeDbHelper.update(sql.toString(), valueList.toArray(new Object[0]));
		return result;
	}

	/**
	 * 根据主健获取数据
	 * @param tableName
	 * @param keyFieldName
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	protected Map<String, Object> getMap(String tableName, String keyFieldName, Object id) throws Exception {
		Map<String, Object> map = null;
		String sql = "select * from "+ tableName +" where  " + keyFieldName + "=?";
		Object[] args = {id};
		map = readDbHelper.getSingle(sql, args);
		return map;
	}
	/**
	 * 通过SQL取某记录的详细信息，一般不推荐使用，除非 getMap(String tableName, String keyFieldName, Object id) 方法不能满足需求
	 * @param sql
	 * @param argList
	 * @return
	 * @throws Exception
	 */
	protected Map<String, Object> getMap(StringBuilder sql, LinkedList<Object> argList) throws Exception {
		Object[] args = new Object[argList.size()];
		for (int i = 0; i < argList.size(); i++) {
			args[i] = argList.get(i);
		}
		return readDbHelper.getSingle(sql.toString(), args);
	}

	/**
	 * 取单条数据
	 *  getMap(String tableName, String keyFieldName, Object id) 的升级版，支持更丰富的查询条件
	 * @param tableName
	 * @param queryMap
	 * @return
	 * @throws Exception
	 */
	protected Map<String, Object> getMap(String tableName, QueryMap queryMap) throws Exception {
		LinkedList<Object> argList = new LinkedList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select *  from " + tableName);
		// 查询条件
		String where = joinWhere(queryMap, argList);
		if(where!=null && where.length()>0) {
			sql.append(" where " + where);
		}
		return readDbHelper.getSingle(sql, argList);
	}


	/**
	 * 删除记录
	 * @param tableName 要删除的记录所有的表
	 * @param keyFieldName 要删除的记录的主键
	 * @param keyFieldValue 要删除的记录的主键id值
	 * @return
	 * @throws Exception
	 */
	protected int delete(String tableName, String keyFieldName, Object keyFieldValue) throws Exception {
		try {
			return writeDbHelper.delObj(tableName, keyFieldName, keyFieldValue);
		} catch(CannotGetJdbcConnectionException e) {
			throw new Exception("数据库连接失败。");
		}
	}

	/**
	 * 删除数据
	 * @param sql 删除语句
	 * @return
	 * @throws Exception
	 */
	protected int delete(String sql, LinkedList<Object> argList) throws Exception {
		Object[] args = new Object[argList.size()];
    	for(int i=0; i<argList.size(); i++){
    		args[i] = argList.get(i);
    	}
		return writeDbHelper.delete(sql, args);
	}

    /**
     * 用于删除拥有联合主键的表中的记录
     * @param tableName
     * @param keyValues
     * @return
     * @throws Exception
     */
	protected int delete(String tableName, Map<String, String> keyValues) throws Exception {
		String delete = "delete from " + tableName;
		StringBuilder where = new StringBuilder();
		Set<String> set = keyValues.keySet();
		String[] params = new String[set.size()];
		int i=0;
		for (String key : set) {
			if(where.length() == 0) {
				where.append(" where ");
			} else {
				where.append(" and ");
			}
			where.append(key).append("=?");
			params[i++] = keyValues.get(key);
		}
		return writeDbHelper.delete(delete + where.toString(), params);
	}
	
	/**
	 * 执行删除操作
	 * @param tableName 要删除数据的表
	 * @param queryMap 要删除的数据的筛选条件
	 * @return
	 * @throws Exception
	 */
	protected int delete(String tableName, QueryMap queryMap) throws Exception {
		LinkedList<Object> argList = new LinkedList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("delete from " + tableName);
		// 查询条件
		String where = joinWhere(queryMap, argList);
		if(where!=null && where.length()>0) {
			sql.append(" where " + where);
		}
		return writeDbHelper.delete(sql, argList);
	}

	/**
	 * 清空表，删除表中所有数据
	 * 执行非常危险
	 * @param tableName 表名
	 * @return
	 */
	protected int clearData(String tableName) {
		return writeDbHelper.delete("delete from " + tableName);
	}
	/**
	 * 直接通过执行SQL获取数据列表
     * 除非  getList(String tableName, String columns, QueryMap queryMap, String sidx, String sord, int start, int limit)  无法满足，否则不建议使用 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	protected List<Map<String, Object>> getList(String sql) throws Exception {
		return readDbHelper.getList(sql);
	}
	/**
	 * 直接通过执行SQL获取数据列表
     * 除非  getList(String tableName, String columns, QueryMap queryMap, String sidx, String sord, int start, int limit)  无法满足，否则不建议使用 
	 * @param sql
	 * @param args
	 * @return
	 * @throws Exception
	 */
	protected List<Map<String, Object>> getList(String sql, Object[] args) throws Exception {
		return readDbHelper.getList(sql, args);
	}
	/**
	 * 直接通过执行SQL获取数据列表
     * 除非  getList(String tableName, String columns, QueryMap queryMap, String sidx, String sord, int start, int limit)  无法满足，否则不建议使用 
	 * @param sql
	 * @param argList
	 * @return
	 * @throws Exception
	 */
	protected List<Map<String, Object>> getList(String sql, LinkedList<Object> argList) throws Exception {
		return readDbHelper.getList(sql, argList);
	}



}
