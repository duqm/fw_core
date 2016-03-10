package net.sanmuyao.core.db;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 读数据库助手，用于执行从数据库中取数据的操作
 * @author 杜庆明  duqingming@qq.com
 */
public class ReadDbHelper {
 
    private List<JdbcTemplate> jdbcTemplateList = null;
    private static int index = 0;
    
    public List<JdbcTemplate> getJdbcTemplateList() {
		return jdbcTemplateList;
	}

	public void setJdbcTemplateList(List<JdbcTemplate> jdbcTemplateList) {
		this.jdbcTemplateList = jdbcTemplateList;
	}

	/**
     * 取jdbcTemplate
     * @return
     */
    protected JdbcTemplate getJdbcTemplate() {
    	if(jdbcTemplateList!=null && jdbcTemplateList.size()>0) {
    		if(index!=0 && index >= (jdbcTemplateList.size()-1)) {
    			index = 0;
    		}
			return jdbcTemplateList.get(index++);
    	}
    	return null;
    }
      
    /**
     * 直接执行SQL语句
     * @param sql
     */
    public void execute(String sql) throws Exception {
    	getJdbcTemplate().execute(sql);
    }



    /**
     * 根据表名和ID取数据的详细信息
     * @param tableName
     * @param idFieldName
     * @param id
     * @return
     * @throws Exception 
     */
	public Map<String, Object> getSingle(String tableName, String idFieldName, Object id) throws Exception {
		String sql = "select * from "+tableName+" where "+idFieldName+"=?";
		Object[] args = {id};
		return getSingle(sql, args);
	}
	
	/**
	 * 根据表名和ID取数据的详细信息
	 * getSingle:  <br/>
	 * TODO<br/>
	 * @author duqingming@126.com
	 * @param tableName
	 * @param fields 要取的字段
	 * @param idFieldName
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getSingle(String tableName, String fields, String idFieldName, Object id) throws Exception {
		String sql = "select "+ fields +" from "+tableName+" where "+idFieldName+"=?";
		Object[] args = {id};
		return getSingle(sql, args);
	}
	
    /**
     * 取单条数据
     * @param sql
     * @return
     */
    public Map<String, Object> getSingle(String sql) throws Exception {
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql);
        if (list.size() > 0) {
            return list.get(0);
        }
        
        return null;
    }
    
    /**
     * 取单条数据
     * @param sql
     * @param argList
     * @return
     * @throws Exception
     */
	public Map<String, Object> getSingle(StringBuilder sql, LinkedList<Object> argList) throws Exception {
		Object[] args = new Object[argList.size()];
		for (int i = 0; i < argList.size(); i++) {
			args[i] = argList.get(i);
		}
		return getSingle(sql.toString(), args);
	}

    /**
     * 取单条数据
     * @param sql
     * @return
     */
    public Map<String, Object> getSingle(String sql, Object[] args) throws Exception {
    	Map<String, Object> data = null;
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, args);
        if (list.size() > 0) {
        	data = list.get(0);
        }
        return data;
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
     * 取数据库连接
     * @return
     * @throws Exception
     */
    public Connection getConn() throws SQLException {
        return getJdbcTemplate().getDataSource().getConnection();
    }


}
