/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sanmuyao.core.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.jdbc.core.PreparedStatementCreator;

/**
 * 创建PreparedStatement的接口定义
 * @author 杜庆明
 */
public class MyPreparedStatementCreator implements PreparedStatementCreator {

    private String strSql;  
    private Object[] params;
  
    public MyPreparedStatementCreator(String strSql, Object[] params) {
        this.strSql = strSql;
        this.params = params; 
    }

    /**
     * 继承的方法
     */
    public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
        java.sql.PreparedStatement ps = conn.prepareStatement(strSql, Statement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < params.length; i++) {
            this.setPreparedStatement(ps, (i + 1), params[i]);
        }
        return ps;
    }

    /**
     * 根据数据类型设置 PreparedStatement
     */
    private void setPreparedStatement(PreparedStatement ps, int parameterIndex, Object param) throws SQLException {
        if (param instanceof Integer) {
            int value = ((Integer) param).intValue();
            ps.setInt(parameterIndex, value);
        } else if (param instanceof String) {
            String s = (String) param;
            ps.setString(parameterIndex, s);
        } else if (param instanceof Double) {
            double d = ((Double) param).doubleValue();
            ps.setDouble(parameterIndex, d);
        } else if (param instanceof Float) {
            float f = ((Float) param).floatValue();
            ps.setFloat(parameterIndex, f);
        } else if (param instanceof Long) {
            long l = ((Long) param).longValue();
            ps.setLong(parameterIndex, l);
        } else if (param instanceof Boolean) {
            boolean b = ((Boolean) param).booleanValue();
            ps.setBoolean(parameterIndex, b);
        } else if (param instanceof java.util.Date) {
            java.sql.Timestamp d = new java.sql.Timestamp(((java.util.Date) param).getTime());
            ps.setTimestamp(parameterIndex, d);
        } else if (param instanceof java.sql.Date) {
            ps.setDate(parameterIndex, (java.sql.Date) param);
        } else if(param instanceof java.math.BigDecimal){
            ps.setBigDecimal(parameterIndex, (java.math.BigDecimal)param);
        } else {
            String s = String.valueOf(param);
            ps.setString(parameterIndex, s);
            System.err.println("没有找到对象：“" + param + "”的数据类型。"+param.getClass());
        }
    }
}
