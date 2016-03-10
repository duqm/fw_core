package net.sanmuyao.core.util;	

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据验证工具类
 * @author 杜庆明
 *
 */
public class ValidateUtil { 
   	
	/**
	 * 验证字符串是否为null或空字符串
	 * 如果字符串中包含字符（包括空格），返回false
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		if(null == str) return true;
		if("".equals(str.trim())) return true;
		return false;
	}
	
	/**
	 * 验证字符串是否为null或空字符串
	 * 如果字符串中包含字符（包括空格），返回true
	 * @param str
	 * @return
	 */
	public static boolean isNotNull(String str) {
		return !isNull(str);
	}
	
	/**
	 * 验证任意的对象为null或为空字符（仅支持基本数据类型）
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj){
		if(null == obj || obj.toString().trim().length()==0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 验证任意的对象不为null并且不为空字符（仅支持基本数据类型）
	 * @param obj
	 * @return
	 */
	public static boolean isNotNull(Object obj)
	{
		return !isNull(obj);
	}
	
	 /**
     * <p>判断map集合是否为null或empty</p>
     * <strong>null或empty返回true，其他返回false</strong>
     * @param map
     * @return
     */
    public static boolean isMapNull(Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            return true;
        }
        return false;
    }
    
    /**
     * <p>判断map集合不为null或empty</p>
     * @param map
     * @return
     */
    public static boolean isNotNullForMap(Map<?, ?> map) {
    	return !isMapNull(map);
    }
    
    /**
     * 判断Set为空
     * @param set
     * @return
     */
    public static boolean isNullForSet(Set<?> set)
    {
    	if(null == set || set.isEmpty())
    	{
    		return true;
    	}
    	return false;
    }
    
    /**
     * 判断Set不为空
     * @param set
     * @return
     */
    public static boolean isNotNullForSet(Set<?> set)
    {
    	return !isNullForSet(set);
    }
    
    /**
     * 数组是为空
     * @param obj
     * @return
     */
    public static boolean isNullForArray(Object [] obj)
    {
    	if(null == obj || "".equals(obj))
    	{
    		return true;
    	}
    	return false;
    }
    
    /**
     * 数组不为空
     * @param obj
     * @return
     */
    public static boolean isNotNullForArray(Object [] obj)
    {
    	return !isNullForArray(obj);
    }

    /**
     * <p>判断集合是否为null或empty</p>
     * <strong>null或empty返回true，其他返回false</strong>
     * @param list
     * @return
     */
    public static boolean isListNull(List<?> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }
    
    /**
     * <p>判断list集合不为null或empty</p>
     * @param list
     * @return
     */
    public static boolean isNotNullForList(List<?> list)
    {
    	return !isListNull(list);
    }

    /**
     * 判断是不是正整数,含不含0
     * @author 李中朋
     * @param num
     * @param ise是否含0
     * @return 符合条件返回true
     */
    public static boolean isInteger(String num,boolean ise){
    	String reg = "";
    	if(ise)
    		reg = "[0-9]\\d*";
    	else{
    		reg = "[1-9]\\d*";
    	}
    	if(ValidateUtil.isNotNull(num)){
    		if(num.matches(reg))
    			return true;
    		else
    			return false;
    	} else {
    		return false;
    	}
    }
    
    /**
     * 判断一个字符串是否都为数字  
     * @author lvjie
     * @param strNum
     * @return
     */
    public static boolean isDigit(String strNum) {  
        return strNum.matches("[0-9]{1,}");  
    } 
    

    /**
     * 判断是否为纯数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher matcher = pattern.matcher(str);
		if(matcher.matches()){
			return true;
		} else {
			return false;
		}
	}
    
	/** 
     * 手机号验证 
     *  
     * @param  str 
     * @return 验证通过返回true 
     */  
    public static boolean isMobile(String str) {   
		if(str == null) return false;
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;   
        p = Pattern.compile("^[1][2,3,4,5,6,7,8,9][0-9]{9}$"); // 验证手机号  
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    }

	/**
	 * 验证输入的邮箱格式是否符合
	 * 
	 * @param email
	 * @return 是否合法
	 */
	public static boolean isEmail(String email) {
		if(email == null) return false;
		boolean tag = true;
		final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}

	/**
	 * 验证日期格式
	 * yyyy-MM-dd
	 * @param parameter
	 * @return
	 */
	public static boolean isDate(String date) {
		if(date == null) return false;
		boolean tag = true;
		final String pattern1 = "^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(date);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}
}
