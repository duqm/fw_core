package net.sanmuyao.core.db;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;

/**
 * 写数据库助手
 * @author 杜庆明 duqingming@qq.com
 */
public class LogDbHelper {

    private JdbcTemplate jdbcTemplate;

    // 为oracle
    private LobHandler lobHandler;

    /**
     * 直接执行SQL语句
     * @param sql
     */
    public void execute(String sql) throws Exception {
        jdbcTemplate.execute(sql);
    }

    /**
     * 执行插入SQL并返回插入数据的ID
     * @param sql
     * @return
     */
    public int insert(String sql) throws Exception {
        final String strSql = sql;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {

                    @Override
                    public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                        java.sql.PreparedStatement ps = conn.prepareStatement(strSql, Statement.RETURN_GENERATED_KEYS);
                        return ps;
                    }
                },
                keyHolder);
        return keyHolder.getKey().intValue();
    }

    /**
     * 添加记录，返回是否添加成功
     * @param sql
     * @return
     */
    public boolean add(String sql) throws Exception {
        return (jdbcTemplate.update(sql) > 0) ? true : false;
    }

    /**
     * 添加记录，返回是否添加成功
     * @param sql
     * @param params
     * @return
     */
    public int execute(String sql, Object[] params) throws Exception {
        return jdbcTemplate.update(sql, params);
    }

    /**
     * 添加记录，返回是否添加成功
     * @param tableName 表名
     * @param entity 实体数据，会调用 entity.keySet() 做为需要插入的数据属性集合
     * @return
     * @throws Exception
     */
    public boolean add(String tableName, Map<String, Object> entity) throws Exception {
    	return this.add(tableName, entity, entity.keySet());
    }

    /**
     * 取添加的SQL
     * @param tableName
     * @param entity
     * @param keys
     * @return
     * @throws Exception
     */
    public Object[] getAddSql(String tableName, Map<String, Object> entity) throws Exception {
        StringBuilder sqlAttr = new StringBuilder();
        StringBuilder sqlValues = new StringBuilder();
        List<Object> values = new LinkedList<Object>();
        for (Object key : entity.keySet()) {
        	if(entity.get(key)==null || "".equals(entity.get(key).toString().trim())){
        		continue;
        	}
            if (sqlAttr.length() != 0) {
                sqlAttr.append(",");
                sqlValues.append(",");
            }
            sqlAttr.append(key);
            sqlValues.append("?");
            values.add(entity.get(key));
        }

        String sql = "insert into " + tableName + "(" + sqlAttr.toString() + ") values(" + sqlValues.toString() + ")";
        Object[] retr = {sql, values};
        return retr;
    }

    /**
     * 添加记录，返回是否添加成功
     * @param tableName 表名
     * @param entity 实体
     * @param keys 表字段集合
     * @return
     * @throws Exception
     */
    public boolean add(String tableName, Map<String, Object> entity, Collection<String> keys) throws Exception {
        StringBuilder sqlAttr = new StringBuilder();
        StringBuilder sqlValues = new StringBuilder();
        List<Object> values = new LinkedList<Object>();
        for (Object key : keys) {
        	if(entity.get(key)==null || "".equals(entity.get(key).toString().trim())){
        		continue;
        	}
            if (sqlAttr.length() != 0) {
                sqlAttr.append(",");
                sqlValues.append(",");
            }
            sqlAttr.append(key);
            sqlValues.append("?");
            values.add(entity.get(key));
        }
        String sql = "insert into " + tableName + "(" + sqlAttr.toString() + ") values(" + sqlValues.toString() + ")";
        Object[] params = values.toArray();
        return (jdbcTemplate.update(sql, params) > 0) ? true : false;
    }

    /**
     * 批量添加记录，返回是否添加成功
     * @param tableName 表名
     * @param entities 实体列表
     * @param keys 表字段集合
     * @return
     * @throws Exception
     */
    public boolean batchInsert(String tableName, List<Map<String, Object>> entities, Collection<String> keys) throws Exception {
        List<Object> values = new ArrayList<Object>();
        List<Object> params = new ArrayList<Object>();

    	for (Map<String, Object> entity : entities) {
    		if (entity == null) continue;

    		List<String> arr = new ArrayList<String>();
    		for (Object key : keys) {
    			arr.add("?");
    			values.add(entity.get(key));
    		}

    		StringBuilder sb = new StringBuilder();
    		sb.append("(");
    		sb.append(StringUtils.join(arr, ","));
    		sb.append(")");
    		params.add(sb.toString());
    	}

        String cols = StringUtils.join(keys, ",");
        String sql = "insert into " + tableName + "(" + cols + ") values " + StringUtils.join(params, ",");
        return (jdbcTemplate.update(sql, values.toArray()) > 0) ? true : false;
    }

    /**
     * 批量添加记录，返回是否添加成功
     * @param tableName 表名
     * @param entities 实体列表
     * @param keys 表字段集合
     * @return
     * @throws Exception
     */
    public boolean batchInsert(String tableName, List<Map<String, Object>> entities) throws Exception {
        if (entities == null || entities.size() == 0) return false;

        Collection<String> keys = entities.get(0).keySet();
        return batchInsert(tableName, entities, keys);
    }

    /**
     * 执行插入SQL并返回插入数据的ID
     * @param sql
     * @param params
     * @return
     */
    public int insert(String sql, final Object[] params) throws Exception {
        final String strSql = sql;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new MyPreparedStatementCreator(strSql, params), keyHolder);
        return keyHolder.getKey().intValue();
    }



    /**
     * 执行删除SQL
     * @param sql
     * @return 删除记录的条数
     */
    public int delete(String sql, Object[] params) throws Exception {
        return jdbcTemplate.update(sql, params);
    }

    public int delete(String sql) {
        return jdbcTemplate.update(sql);
    }

    /**
     * 更新数据
     * @param tableName
     * @param dataMap
     * @param idField
     * @param id
     * @return
     * @throws Exception
     */
	public boolean update(String tableName, Map<String, Object> dataMap, String idField, Object id) throws Exception {
		List<String> fields = new ArrayList<String>();
		Iterator<String> i = dataMap.keySet().iterator();
		while(i.hasNext()) {
			fields.add(i.next());
		}
		return update(tableName, dataMap, fields, idField, id);
	}

    /**
     * 更新记录
     * @param tableName
     * @param dataMap
     * @param fields
     * @param idField
     * @param id
     * @return
     * @throws Exception
     */
	public boolean update(String tableName, Map<String, Object> dataMap, List<String> fields, String idField, Object id) throws Exception {
		Map<String, Object> keyValues = new HashMap<String, Object>();
		keyValues.put(idField, id);
		return update(tableName, dataMap, fields, keyValues);
	}

	/**
	 * 更新数据
	 * @param tableName
	 * @param dataMap
	 * @param keyValues
	 * @return
	 * @throws Exception
	 */
	public boolean update(String tableName, Map<String, Object> dataMap, Map<String, Object> keyValues) throws Exception {
		List<String> fields = new ArrayList<String>();
		Iterator<String> i = dataMap.keySet().iterator();
		while(i.hasNext()) {
			fields.add(i.next());
		}
		return update(tableName, dataMap, fields, keyValues);
	}

	/**
	 * 更新数据
	 * @param tableName
	 * @param dataMap
	 * @param fields
	 * @param keyValues　要更新的数据主键及值
	 * @return
	 * @throws Exception
	 */
	public boolean update(String tableName, Map<String, Object> dataMap, List<String> fields, Map<String, Object> keyValues) throws Exception {
		Map<String, Object> map = makePrepareMap(tableName, dataMap, fields, keyValues);
		String sql = (String) map.get("sql");
		Object[] param = (Object[]) map.get("param");
		return update(sql, param) ;
	}

    /**
     * 执行更新SQL
     * @param sql
     * @return 更新是否成功
     */
    public boolean update(String sql) throws Exception {
        return (jdbcTemplate.update(sql) > 0) ? true : false;
    }

    /**
     * 执行更新SQL
     * @param sql 更新SQL语句
     * @param params 参数
     * @return 更新是否成功
     */
    public boolean update(String sql, Object[] params) throws Exception {
        return (jdbcTemplate.update(sql, params) > 0) ? true : false;
    }

    /**
     * 批量执行
     * @param sqls
     * @return
     * @throws Exception
     */
    public int[] update(String[] sqls) throws Exception {

        return jdbcTemplate.batchUpdate(sqls);
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
    public void batchUpdate(String tableName, List<Map<String, Object>> dataList, List<String> fields, List<Map<String, Object>> condList) throws Exception {
        int dataLen = dataList.size(), condLen = condList.size();
        if (dataLen != condLen) return;


        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < dataLen; i++) {
            Map<String, Object> dataMap = dataList.get(i);
            Map<String, Object> condMap = condList.get(i);
            Map<String, Object> map = makePrepareMap(tableName, dataMap, fields, condMap);
            mapList.add(map);
        }

        batchUpdateWithParams(mapList);
    }

    /**
     * Makes sql / param in prepare statement.
     *
     * @param tableName
     * @param dataMap
     * @param fields
     * @param condMap
     * @return
     */
    private static Map<String, Object> makePrepareMap(String tableName, Map<String, Object> dataMap,
            List<String> fields, Map<String, Object> condMap) {
        if (fields == null || fields.size() == 0) {
            fields = new ArrayList<String>();
            fields.addAll(dataMap.keySet());
        }

        StringBuilder fieldbuffer = new StringBuilder();
        List<Object> paramList = new ArrayList<Object>();
        for(String field : fields){
            if(fieldbuffer.length()!=0){
                fieldbuffer.append(",");
            }
            fieldbuffer.append(field+"=?");
            paramList.add(dataMap.get(field));
        }

        StringBuilder sqlBuffer = new StringBuilder();
        sqlBuffer.append("update "+tableName+" set "+fieldbuffer.toString());

        // 更新条件
        Iterator<String> keys = condMap.keySet().iterator();
        String key = null;
        int i = 0;
        while(keys.hasNext()) {
            key = keys.next();
            paramList.add(condMap.get(key));
            if(i==0) {
                sqlBuffer.append(" where " +  key + "=?");
            } else {
                sqlBuffer.append(" and " +  key + "=?");
            }
            i++;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sql", sqlBuffer.toString());
        map.put("param", paramList.toArray());
        return map;
    }

    /**
     * 批量执行INSERT或UPDATE语句，允许参数
     * @param sqlAndParams 参数：list中存放MAP，map中分sql(String)和(Object[])param两个KEY对应每条SQL和参数
     * @return
     * @throws Exception
     */
    public void batchUpdateWithParams(List<Map<String, Object>> sqlAndParams) throws Exception {
    	Connection conn=jdbcTemplate.getDataSource().getConnection();
    	conn.setAutoCommit(false);
    	PreparedStatement pt=null;
    	for(Map<String, Object> map:sqlAndParams){
    		String sql=""+map.get("sql");
    		Object[] p=(Object[])map.get("param");
    		pt = conn.prepareStatement(sql);
			if (p != null) {
				for (int j = 0; j < p.length; j++) {
					pt.setObject(j + 1, p[j]);

				}
			}
			pt.execute();

    	}
    	pt.close();
    	conn.commit();
    	conn.close();
    }

    /**
     * 取单条数据
     * @param sql
     * @return
     */
    public Map<String, Object> getSingle(String sql) throws Exception {
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 执行查询SQL
     * @param sql
     * @return
     */
    public List<Map<String, Object>> getList(String sql) throws Exception {
        List<Map<String, Object>> list = null;
        list = getJdbcTemplate().queryForList(sql);
        return list;
    }


    /**
     * 执行查询SQL
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getList(StringBuilder sql, LinkedList<Object> argList) throws Exception {
        return getList(sql.toString(), argList);
    }
    
    public List<Map<String, Object>> getList(String sql, LinkedList<Object> argList) throws Exception {
    	Object[] args = new Object[argList.size()];
    	for(int i=0; i<argList.size(); i++){
    		args[i] = argList.get(i);
    	}
        return getList(sql.toString(), args);
    }
    
    /**
     * 执行查询SQL
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getList(String sql, Object[] args) throws Exception {
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, args);
        return list;
    }

    /**
     * 根据SQL返回记录数
     * @param sql select count(*) from XXXX where XXXX=XXX
     * @return
     */
    public Long getTotal(StringBuilder sql, LinkedList<Object> argList) throws Exception {
    	Object[] args = new Object[argList.size()];
    	for(int i=0; i<argList.size(); i++){
    		args[i] = argList.get(i);
    	}
    	return getTotal(sql.toString(), args);
    }
	
    /**
     * 根据SQL返回记录数
     * @param sql select count(*) from XXXX where XXXX=XXX
     * @return
     */
    public Long getTotal(String sql, Object[] args) throws Exception {
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, args);
        if (list.size() > 0) {
            Map<String, Object> m = (Map<String, Object>) list.get(0);
            return (Long)m.values().iterator().next();
        } else {
            return Long.valueOf("0");
         }
    }

    /**
     * 根据SQL返回记录数
     * @param sql select count(*) from XXXX where XXXX=XXX
     * @return
     */
    public int getTotal(String sql) throws Exception {
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql);
        if (list.size() > 0) {
            Map<String, Object> m = (Map<String, Object>) list.get(0);
            return Integer.parseInt(String.valueOf(m.values().iterator().next()));
        } else {
            return 0;
        }
    }

    /**
     * <p>调用存储过程返回自增ID</p>
     * <strong>调用存储过程返回自增ID</strong>
     * @author licw
     * @param tableName 表名
     * @return long 自增ID
     */
    public long getNextId(String tableName) throws SQLException {

        final String param1 = tableName.toUpperCase();
        final int param2 = 1 + (int) (Math.random() * 9); //生存随机数

        String nextId = (String) jdbcTemplate.execute(
                new CallableStatementCreator() {

                    @Override
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call FYZ_Proc_GetNextID(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, param1);// 设置输入参数的值.
                        cs.setInt(2, param2);// 设置输入参数的值
                        cs.registerOutParameter(3, Types.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                },
                new CallableStatementCallback() {

                    @Override
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        return cs.getString(3);// 获取输出参数的值
                    }
                });

        return Long.valueOf(nextId);
    }

	/**
	 * Oracle 更新Clob字段值
	 *
	 * @param sql
	 *            Value值用?号表示
	 * @param strArg
	 *            ? 号对应的值，数据的顺序就是？的顺序
	 */
	public void clobExcute(String sql, final String[] strArg) {
		if (strArg != null && strArg.length > 0) {
			this.jdbcTemplate.execute(sql, new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
				@Override
				protected void setValues(PreparedStatement pstm, LobCreator lobCreator) throws SQLException, DataAccessException {
					for (int i = 0; i < strArg.length; i++) {
						lobCreator.setClobAsString(pstm, i + 1, strArg[i]);
					}
				}
			});
		}
	}

	/**
	 * Oracle 取Clob字段值
	 *
	 * @param sql
	 */
	public String clobQuery(String sql) {
		final StringBuffer sb = new StringBuffer();
		this.jdbcTemplate.query(sql, new AbstractLobStreamingResultSetExtractor() {
			@Override
            protected void streamData(ResultSet rs) throws SQLException, IOException, DataAccessException {
				Reader rd = lobHandler.getClobAsCharacterStream(rs, 1);
				if (rd != null) {
					BufferedReader br = new BufferedReader(rd);
					String line;
					while ((line = br.readLine()) != null) {
						sb.append(line);
					}
				}
			}
		});
		return sb.toString();
	}

    /**
     * 取数据库连接
     * @return
     * @throws Exception
     */
    public Connection getConn() throws SQLException {
        return jdbcTemplate.getDataSource().getConnection();
    }

    // =========================
    /**
     * @return the jdbcTemplate
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * @param jdbcTemplate the jdbcTemplate to set
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 删除指定表中的指定记录
     * @param tableName
     * @param keyFieldName
     * @param keyFieldValue
     * @return
     * @throws Exception
     */
	public int delObj(String tableName, String keyFieldName, Object keyFieldValue) throws Exception {
		String sql = "delete from "+tableName+" where "+keyFieldName+"=?";
		Object[] parms = {keyFieldValue};
		return delete(sql, parms);
	}

	public LobHandler getLobHandler() {
		return lobHandler;
	}

	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}

}
