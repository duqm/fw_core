package net.sanmuyao.core.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 数据类型转换器
 * @author 杜庆明
 *
 */
public class ConverterUtil {

	/**
	 * 将字段串中的特殊字符进行转意，使其能够在页面上显示
	 * @param obj
	 * @return
	 */
	public static String toHTML(Object obj) {
		if( obj == null ) {
			return "";
		} else {
			return toHTML(obj.toString());
		}
	}
	/**
	 * 将字段串中的特殊字符进行转意，使其能够在页面上显示
	 * @param string
	 * @return
	 */
	public static String toHTML(String str) {
		if(str==null){
			return "";
		}
        str = str.replace(" ", "&nbsp;");
        str = str.replace("　", "&nbsp;&nbsp;");
        str = str.replace("\\", "\\\\");
        str = str.replaceAll("\"", "\\\\\"");
        str = str.replaceAll("'", "\\\\\'");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("[\\n\\r]", "<br/>");
        str = str.replace("\\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		return str;
	}

	/**
	 * 将文本内容中的特殊字符进行转意
	 * @param obj
	 * @return
	 */
	public static String toText(Object obj) {
		if(obj ==null ){
			return "";
		}
		String str = String.valueOf(obj);
        str = str.replace("\\", "\\\\");
        str = str.replaceAll("\"", "\\\\\"");
        str = str.replaceAll("'", "\\\\\'");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("[\\n\\r]", "\\\\r\\\\n");
        str = str.replace("\\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		return str;
	}


	/**
	 * 格式化字符串，将第一个字符串中的“{n}”替换为后面对应位置的内容
	 * @param str
	 * @param args
	 * @return
	 */
	public static String format(String str, Object ... args){
		if(str == null)
			return "";
		StringBuilder newstr = new StringBuilder();
		char[] cs = str.toCharArray();
		int start = -1;
		int end = -1;
		String strIndex = null;
		int index = 0;
		int i = 0;
		boolean add = true;
		for(char c : cs){
			if(c == '{'){
				if(start >= 0){
					// 上次未关闭，将所有字段串压入
					String s = str.substring(start-1, i);
					newstr.append(s);
				}
				start = i+1;
				add = false;
			} else if(c == '}'){
				end = i;
				// 其中一个要替换的位置
				strIndex = str.substring(start, end);
				index = Integer.parseInt(strIndex.trim());
				if(index >= args.length){
					System.err.println("\r\n ========= 位置：" + index +" 处的内容无法被替换。\r\n\r\n" );
					newstr.append("{"+strIndex+"}");
					start = -1;
					i++;
					continue;
				}
				newstr.append(args[index].toString());
				add = true;
				start = -1;
				i++;
				continue;
			}
			if(add){
				newstr.append(c);
			}
			i++;
		}
		return newstr.toString();
	}
	/**
	 * 将对象类型转成字符串类型
	 * @param object
	 * @return
	 */
	public static String toString(Object obj) {
		if(obj == null) {
			return "";
		} else if(obj instanceof String ){
			return (String)obj;
		} else {
			return obj.toString();
		}
	}
	
	public static void main(String[] args) {
		System.out.println(new Date());
	}

	public static boolean toBool(Object value) {
		if (value != null) {
			if (value instanceof Boolean) {
				return (Boolean) value;
			}

			if (value instanceof Integer) {
				Integer integer = (Integer) value;
				return integer.intValue() == 1;
			}

			if (value instanceof String) {
				if (((String) value).length() == 0) {
					return false;
				} else {
					return "true".equalsIgnoreCase((String) value)
							|| "1".equals(value);
				}
			}
		}
		return false;
	}

	 /**
     * <p>将对象转换为int</p>
     * <strong>将对象转换为int, NULL返回0</strong>
     * @param obj 对象
     * @return int
     */
    public static int toInt(Object obj) {
        int i = 0;
        if (obj == null || "".equals(obj.toString().trim())) {
            return i;
        }
        if (obj instanceof Integer) {
            i = (Integer) obj;
        } else if (obj instanceof Long) {
            i = Integer.parseInt(String.valueOf((obj)));
        } else if (obj instanceof Double) {
            i = (int) ((Double) obj).doubleValue();
        } else {
            String str = obj.toString();
            if("true".equals(str)) {
            	return 1;
            } else if("false".equals(str)) {
            	return -1;
            }
            // 判断是否是浮点数格式，如果是去除小数部分
            if (str.indexOf(".") > -1) {
                str = str.substring(0, str.indexOf("."));
            }
            try {
                i = Integer.valueOf(str);
            } catch (Exception e) {
                // 类型转换异常
                e.printStackTrace();
            }
        }
        return i;
    }

	/**
	 * 将对象转变成Long类型，如果obj为空则返回Long类型的最小值-9223372036854775808
	 * @param obj
	 * @return
	 */
	public static Long toLong(Object obj) {
		if(obj == null || obj.toString().trim().equals("null") || obj.toString().trim().length()==0) {
			return Long.parseLong("0");
		} else if(obj instanceof Long ){
			return (Long)obj;
		} else {
			return Long.parseLong(obj.toString());
		}
	}

	public static BigDecimal toBigDecimal(Object obj) {
		if(obj == null || obj.toString().length()==0) {
			return BigDecimal.ZERO;
		} else if(obj instanceof Long ){
			return (BigDecimal)obj;
		} else {
			return BigDecimal.valueOf(Long.parseLong(obj.toString()));
		}
	}

	public static Float toFloat(Object obj) {
		if(obj == null || obj.toString().length()==0) {
			return Float.parseFloat("0");
		} else if(obj instanceof Float ){
			return (Float)obj;
		} else {
			return Float.parseFloat(obj.toString());
		}
	}

	/**
     * <p>将对象转换为Double</p>
     * <strong>将对象转换为double, NULL返回0.0</strong>
     * @param obj 对象
     * @return double
     */
    public static double toDouble(Object obj) {
        double dou = 0.0;
        if (obj == null || "".equals(obj.toString().trim())) {
            return dou;
        }
        if(obj instanceof Double) {
        	dou = (Double)obj;
        } else if(obj instanceof BigDecimal) {
        	dou = ((BigDecimal)obj).doubleValue();
        } else {
	        String str = obj.toString();
	        try {
	            dou = Double.parseDouble(str);
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
        }
        return dou;
    }

    /**
     * 转换成金额
     * @param amount
     * @return
     */
	public static double toMoney(Object amount) {
		double dou = toDouble(amount);
		BigDecimal bg = new BigDecimal(dou);
        double rs = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return rs;
	}

    /**
     * 将数据转成布尔值
     * 如果数据为null，直接返回false
     * 如果数据为数值，数值大于0返回true，否则返回false
     * @param object
     * @return
     */
	public static Boolean toBit(Object object) {
		if(object==null) {
			return false;
		} else if(object instanceof Boolean) {
			return (Boolean)object;
		} else if(object instanceof Integer) {
			return (Integer)object > 0;
		} else if(object instanceof Long) {
			return (Long)object > 0;
		} else if(object instanceof BigInteger) {
			return ((BigInteger)object).longValue() > 0;
		} else if(object instanceof BigDecimal) {
			return ((BigDecimal)object).longValue() > 0;
		} else if(object instanceof String) {
			if(object.toString().equalsIgnoreCase("true") || object.toString().equalsIgnoreCase("t")) {
				return true;
			} else if(object.toString().equalsIgnoreCase("false") || object.toString().equalsIgnoreCase("f")) {
				return false;
			} else {
				return Integer.parseInt((String)object) > 0;
			}
		}
		return null;
	}

	 /**
     * 将json格式的字符串转换成list
     * @param json
     * @return
     */
    public  static  List<Map<String, Object>> json2List(String json) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (ValidateUtil.isNull(json)) {
            return list;
        }
        JSONObject jsonObj = null;
        String res = json.substring(1, json.length() - 1);
        String[] ress = res.split("\\},\\{");
        for (String s : ress) {
            if (s.trim().isEmpty()) {
                continue;
            }
            if (!s.startsWith("{")) {
                s = "{" + s;
            }
            if (!s.endsWith("}")) {
                s = s + "}";
            }
            jsonObj = JSONObject.fromObject(s);
            list.add(jsonObj);
        }
        return list;
    }

	/**
	 * 把json对象转换为 Map 对象
	 * @author lvjie
	 * @param jsonStr
	 * @return
	 */
    public static Map<String, Object> parseJSON2Map(String jsonStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 最外层解析
		JSONObject json = JSONObject.fromObject(jsonStr);
		for (Object k : json.keySet()) {
			Object v = json.get(k);
			// 如果内层还是数组的话，继续解析
			if (v instanceof JSONArray) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Iterator<JSONObject> it = ((JSONArray) v).iterator();
				while (it.hasNext()) {
					JSONObject json2 = it.next();
					list.add(parseJSON2Map(json2.toString()));
				}
				map.put(k.toString(), list);
			} else {
				map.put(k.toString(), v);
			}
		}
		return map;
	}

    /**
	 * 全角转半角的 转换函数
	* @Methods Name full2HalfChange
	 * @param QJstr
	 * @return String
	 *  * @author mlh
	 */
	 public static String full2HalfChange(String QJstr) {
		 StringBuffer outStrBuf = new StringBuffer("");
		 String Tstr = "";
		 byte[] b = null;
		 for (int i = 0; i < QJstr.length(); i++) {
			 Tstr = QJstr.substring(i, i + 1);
			 // 全角空格转换成半角空格
			 if (Tstr.equals("　")) {
				 outStrBuf.append(" ");
				 continue;
			 }
			 try {
				 b = Tstr.getBytes("unicode");
				 // 得到 unicode 字节数据
				 if (b[2] == -1) {
				 // 表示全角
					 b[3] = (byte) (b[3] + 32);
					 b[2] = 0;
					 outStrBuf.append(new String(b, "unicode"));
				 } else {
					 outStrBuf.append(Tstr);
				 }
			 } catch (UnsupportedEncodingException e) {
				 e.printStackTrace();
			 }
		 }
		 return outStrBuf.toString();
	 }



	/**
	 * 补齐代码
	 * @param code
	 * @param length
	 * @return
	 */
	public static String getCode(String code, int length) {
		String str = String.valueOf(code);
		if (length > 0) {
			int step = length - str.length();
			while (step-- > 0) {
				str = "0" + str;
			}
		}
		return str;
	}

	/**
	 * 日期时间类型转换
	 * 如果参数不正确，返回 1970-01-01 08:00:00
	 * @param obj Date类型或long类型的日期，或日期的字符串格式（yyyy-MM-dd HH:mm:ss）
	 * @return
	 */
	public static Date toDate(Object obj) {
		if(ValidateUtil.isNull(obj)) {
			return null;
		}
		String pattern = "yyyy-MM-dd";
		DateFormat format = new SimpleDateFormat(pattern);
		String str = "1970-01-01";
		try {
			return toDate(obj, pattern, format.parse(str));
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}
	
	/**
	 * 将未知格式的时间对象转成Date类型的对象
	 * @param obj
	 * @return
	 */
	public static Date toDatetime(Object obj) {
		if(ValidateUtil.isNull(obj)) {
			return null;
		}
		String pattern = "yyyy-MM-dd HH:mm:ss";
		DateFormat format = new SimpleDateFormat(pattern);
		String str = "1970-01-01 08:00:00";
		try {
			return toDate(obj, pattern, format.parse(str));
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	/**
	 * 将未知格式的时间对象转成Date类型的对象
	 * 如果参数不正确，返回 1970-01-01 08:00:00
	 * @param obj Date类型或long类型的日期，或日期的字符串格式（yyyy-MM-dd HH:mm:ss）
	 * @param date 缺省情况下的值，即obj为空时返回的时间值
	 * @return
	 */
	public static Date toDate(Object obj, String pattern, Date date) {
		if(ValidateUtil.isNull(pattern)) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		DateFormat format = new SimpleDateFormat(pattern);
		String str = "";
		if(obj == null) {
			return date;
		} else if(obj instanceof Long) {
			return new Date((Long)obj);
		} else if(obj instanceof Date) {
			return (Date)obj;
		} else {
			str = (String)obj;
		}
		try {
			return format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}
}
