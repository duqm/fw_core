package net.sanmuyao.core.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;


/**
 * 日期时间操作类
 * @author 杜庆明 [duqingming@qq.com]
 * @version 1.0
 */
public class DateUtil {


	/**
	 * 取系统默认时区的当前时间
	 * @return
	 */
	public static Date getDate() {
		// Etc/GMT 是格林威治标准时间,得到的时间和默认时区是一样的
		// Etc/GMT+8 比林威治标准时间慢8小时,
		// Etc/GMT-8 东八区,我们比那快8小时所以减8
		// PRC 设置中国时区
		// Calendar.getInstance(TimeZone.getTimeZone("GMT-8:00")).getTime();
		Calendar cal = Calendar.getInstance(java.util.Locale.CHINA);
		return cal.getTime();
	}

	/**
	 * 取系统默认时区的当前时间毫秒数
	 * @return
	 */
	public static long getTime() {
		return getDate().getTime();
	}

	// =================== start 将日期转成字符串 start =================== //

	/**
	 * 将指定时间转换成指定格式的字符串
	 * 应尽量避免使用此方法，建议使用方法：getStrDateTime(Date date, DateTimeFormat dateTimePattern)
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getStrDateTime(Date date, String format) {
		if (date == null)
			return "";
		DateFormat sdf = new SimpleDateFormat(format);
		String tsStr = sdf.format(date);
		return tsStr;
	}

	/**
	 * 将指定long类型的时间戳转换成指定格式的字符串
	 * 应尽量避免使用此方法，建议使用方法：getStrDateTime(Date date, DateTimeFormat dateTimePattern)
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getStrDateTime(long timeInMillis, String format) {
		if (timeInMillis <= 0)
			return null;
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTimeInMillis(timeInMillis);
		DateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(c.getTime());
	}

	/**
	 * 将指定时间转换成指定格式的字符串
	 * @param date
	 * @param dateTimePattern
	 * @return
	 */
	public static String getStrDateTime(Date date, DateTimePattern dateTimePattern) {
		return getStrDateTime(date, dateTimePattern.getCode());
	}

	/**
	 * 取当指定时间格式化形式的字符串，日期格式为：yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String getStrDateTime(Date date) {
		return getStrDateTime(date, DateTimePattern.shortDateTimePattern);
	}

	/**
	 * 将long类型的时间戳转换成指定格式的字符串，日期格式为：yyyy-MM-dd HH:mm:ss
	 * @param timeInMillis
	 * @return
	 */
	public static String getStrDateTime(long timeInMillis) {
		return getStrDateTime(timeInMillis, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 取指定时间的日期格式化字符串，日期格式为：yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String getStrDate(Date date) {
		return getStrDateTime(date, DateTimePattern.shortDatePattern);
	}

	/**
	 * 取指定时间的时间格式化字符串，时间格式为：HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String getStrTime(Date date) {
		return getStrDateTime(date, DateTimePattern.shortTimePattern);
	}


	/**
	 * 取当前时间的日期格式化字符串，日期格式为：yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String getStrDate() {
		return getStrDateTime(DateUtil.getDate(), DateTimePattern.shortDatePattern);
	}

	/**
	 * 取当前时间的时间格式化字符串，时间格式为：HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String getStrTime() {
		return getStrDateTime(DateUtil.getDate(), DateTimePattern.shortTimePattern);
	}


	/**
	 * 取格式化的系统当前时间，格式为：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getStrDateTime() {
		return getStrDateTime(DateUtil.getDate());
	}


	// =================== end 将日期转成字符串 end =================== //


	// =================== start 取时间 start =================== //

	/**
	 * 将字符串格式的时间转换成时间对象
	 * 不建议直接使用此方法，请使用方法：getDate(String dateString, DateTimeFormat dateTimePattern)
	 * @param dateString
	 * @param dateFormat
	 * @return
	 */
	public static Date getDate(String dateString, String dateFormat) {
		if (StringUtils.isEmpty(dateString)) return null;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return date;
	}

	/**
	 * 将字符串格式的时间转换成时间对象
	 * @param dateString 字符串格式的时间
	 * @param dateTimePattern 时间的格式
	 * @return
	 */
	public static Date getDate(String dateString, DateTimePattern dateTimePattern) {
		return getDate(dateString, dateTimePattern.getCode());
	}

	/**
	 * 将字符串格式的时间转换成时间对象，时间格式必需为：yyyy-MM-dd
	 * @param dateString
	 * @return
	 */
	public static Date getDate(String dateString) {
		return getDate(dateString, DateTimePattern.shortDatePattern);
	}

	/**
	 * 将字符串类型日期转换成日期（格式：yyyy-MM-dd hh:mm:ss)
	 *
	 * @param dateString
	 * @return
	 */
	public static Date getDatetime(String dateString) {
		return getDate(dateString, DateTimePattern.shortDateTimePattern);
	}

	/**
	 * 将Object类型日期转换成日期
	 * @param dateObject
	 * @return
	 */
	public static Date getDateTime(Object dateObject) {
		if (dateObject == null) {
			return null;
		} else if(dateObject instanceof Date) {
			return (Date)dateObject;
		} else if(dateObject instanceof Long) {
			return new Date((Long)dateObject);
		} else if(dateObject instanceof String) {
			return getDatetime(String.valueOf(dateObject));
		}
		return null;
	}

	// =================== end 取时间 end =================== //

	/**
	 * 将Timestamp类型日期转换成long
	 * @param timestamp
	 * @return
	 */
	public static long getLong(Timestamp timestamp) {
		if (timestamp == null)
			return 0;
		return timestamp.getTime();
	}


	/**
	 * 将日期往后推
	 * 设置日期后会将时、分设置为0
	 * @param days 要往后推的天数，如果往前推请传入负数
	 * @return
	 */
	public static Date getAfterDate(int days) {
		Calendar cal = Calendar.getInstance(java.util.Locale.CHINA);
		cal.add(Calendar.DATE, -days);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		return cal.getTime();
	}

	/**
	 * 取系统默认时区的当前年
	 */
	public static int getYear() {
		// Etc/GMT 是格林威治标准时间,得到的时间和默认时区是一样的
		// Etc/GMT+8 比林威治标准时间慢8小时,
		// Etc/GMT-8 东八区,我们比那快8小时所以减8
		// PRC 设置中国时区
		// Calendar.getInstance(TimeZone.getTimeZone("GMT-8:00")).getTime();
		Calendar cal = Calendar.getInstance(java.util.Locale.CHINA);
		int year = cal.get(Calendar.YEAR); //Calendar.MONTH
		return year;
	}

	/**
	 * 取系统默认时区的当前月
	 */
	public static int getMonth() {
		// Etc/GMT 是格林威治标准时间,得到的时间和默认时区是一样的
		// Etc/GMT+8 比林威治标准时间慢8小时,
		// Etc/GMT-8 东八区,我们比那快8小时所以减8
		// PRC 设置中国时区
		// Calendar.getInstance(TimeZone.getTimeZone("GMT-8:00")).getTime();
		Calendar cal = Calendar.getInstance(java.util.Locale.CHINA);
		int month = cal.get(Calendar.MONTH) + 1; //Calendar.MONTH
		return month;
	}

	/**
	 * 取系统默认时区的当前日
	 * @return
	 */
	public static int getDay() {
		// Etc/GMT 是格林威治标准时间,得到的时间和默认时区是一样的
		// Etc/GMT+8 比林威治标准时间慢8小时,
		// Etc/GMT-8 东八区,我们比那快8小时所以减8
		// PRC 设置中国时区
		// Calendar.getInstance(TimeZone.getTimeZone("GMT-8:00")).getTime();
		Calendar cal = Calendar.getInstance(java.util.Locale.CHINA);
		int date = cal.get(Calendar.DATE); //Calendar.DATE
		return date;
	}

	/**
	 * 获取传入时间月份的第一天
	 * @param date
	 * @return
	 */
	public static String getMonthFirstDate(Date date) {
		Calendar cal = Calendar.getInstance();
		  cal.setTime(date);
		  cal.set(Calendar.DAY_OF_MONTH, 1);
		  Date firstDate = cal.getTime();
		  SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-dd");
		  return sf.format(firstDate);
	}

	/**
	 * 获取传入时间月份的最后一天
	 * @param date
	 * @return
	 */
	public static String getMonthLastDate(Date date) {
		 Calendar cal = Calendar.getInstance();
		  cal.setTime(date);
		  cal.add(Calendar.MONTH, 1);
		  cal.set(Calendar.DAY_OF_MONTH, 1);
		  cal.add(Calendar.DAY_OF_MONTH, -1);
		  Date lastDate = cal.getTime();
		  SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-dd");
		  return sf.format(lastDate);
	}

	/**
	 * 获取传入时间下个月第一天
	 * @param dt
	 * @return
	 */
	public static String getNextMonthFirstDate(Date dt) {
		 Calendar cal = Calendar.getInstance();
		  cal.setTime(dt);
		  cal.add(Calendar.MONTH, 1);
		  cal.set(Calendar.DAY_OF_MONTH, 1);
		  Date lastDate = cal.getTime();
		  SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-dd");
		  return sf.format(lastDate);
	}

	/**
	 * 获取传入时间下一个年份第一天
	 * @param dt
	 * @return
	 */
	public static String getNextYearFirstDate(Date dt) {
		 Calendar cal = Calendar.getInstance();
		  cal.setTime(dt);
		  cal.add(Calendar.YEAR, 1);
		  cal.set(Calendar.MONTH, 0);
		  cal.set(Calendar.DAY_OF_MONTH, 1);
		  Date firstDate = cal.getTime();
		  SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-dd");
		  return sf.format(firstDate);
	}

	/**
	 * 获取传入时间当前年份第一天
	 * @param dt
	 * @return
	 */
	public static String getYearFirstDate(Date dt) {
		 Calendar cal = Calendar.getInstance();
		  cal.setTime(dt);
		  cal.set(Calendar.MONTH, 0);
		  cal.set(Calendar.DAY_OF_MONTH, 1);
		  Date firstDate = cal.getTime();
		  SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-dd");
		  return sf.format(firstDate);
	}


	/**
	 * 时间类型的对象转换成字符串类型的时间格式 yyyy-MM-dd HH:mm:ss
	 *
	 * @param date
	 * @return
	 */
	public static String toString(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 时间类型的对象转换成字符串类型的时间格式 yyyy-MM-dd HH:mm:ss
	 *
	 * @param timestamp
	 * @return
	 */
	public static Object toString(Timestamp timestamp) {
		if (timestamp == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(timestamp);
	}

	/**
	 * 两个时间相差距离多少天多少小时多少分多少秒
	 *
	 * @param str1
	 *            时间参数 1 格式：1990-01-01 12:00:00
	 * @param str2
	 *            时间参数 2 格式：2009-01-01 12:00:00
	 * @return long[] 返回值为：{天, 时, 分, 秒}
	 */
	public static long[] getDistanceTimes(String str1, String str2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000) - day * 24);
			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long[] times = { day, hour, min, sec };
		return times;
	}

	/**
	 * 获得当前输入年月的所有日期
	 *
	 * @param year_month
	 * @author 罗静
	 * @return
	 */
	public static List<Object> getAllDate(String year_month) {
		List<Object> list = new ArrayList<Object>();
		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM");
		try {
			rightNow.setTime(simpleDate.parse(year_month));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int days = rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);
		String date = "";
		for (int i = 1; i <= days; i++) {
			if (i < 10) {
				date = year_month + "-0" + i;
			} else {
				date = year_month + "-" + i;
			}
			list.add(date);
		}
		return list;
	}

	/**
	 * 获取当前传入日期(yyyy-MM-dd)是星期几
	 * 修改信息：Miaoshun，2014年12月10日，周日返回7
	 * @param datetime
	 * @author 罗静
	 * @return
	 */
	public static int getWeekdayOfDateTime(Date datetime) {
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(datetime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int weekday = c.get(Calendar.DAY_OF_WEEK) - 1;
		// 星期天
        if (weekday == 0) {
            weekday = 7;
        }
		return weekday;
	}

	/**
	 * 获取星期天数
	 * @return
	 */
	public static int getWeekday() {
		Calendar c = Calendar.getInstance();
		int weekday = c.get(Calendar.DAY_OF_WEEK) - 1;
		// 星期天
		if (weekday == 0) {
			weekday = 7;
		}
		return weekday;
	}
	/**
	 * 获取传入日期所在周期，星期一的时间
	 * @param dt
	 * @return
	 */
	public static String getWeek1day(Date dt){
		Calendar cal = Calendar.getInstance();
		  cal.setTime(dt);
		  cal.set(Calendar.DAY_OF_WEEK,2);
		  Date firstDate = cal.getTime();
		  SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-dd");
		  return sf.format(firstDate);
	}
	/**
	 * 获取传入时间，下一个周一的日期
	 * @param dt
	 * @return
	 */
	public static String getNextWeek1day(Date dt){
		Calendar cal = Calendar.getInstance();
		  cal.setTime(dt);
		  cal.set(Calendar.DAY_OF_WEEK,2);
		  cal.add(Calendar.DAY_OF_MONTH,7);
		  Date firstDate = cal.getTime();
		  SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-dd");
		  return sf.format(firstDate);
	}

	/**
	 * 获取传入日期所在周期，星期日的时间
	 * @param dt
	 * @return
	 */
	public static String getWeek0day(Date dt){
		Calendar cal = Calendar.getInstance();
		  cal.setTime(dt);
		  cal.set(Calendar.DAY_OF_WEEK,1);
		  Date firstDate = cal.getTime();
		  SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-dd");
		  return sf.format(firstDate);
	}

	/**
	 * 获取当前传入日期的前n天日期集合
	 *
	 * @param datetime
	 *            ,n
	 * @author 罗静
	 * @return
	 */
	public static List<String> getDateBefore(Date date, int n) {
		List<String> dateList = new ArrayList<String>();
		Calendar now = Calendar.getInstance();
		for (int i = n; i > 0; i--) {
			now.setTime(date);
			now.set(Calendar.DATE, now.get(Calendar.DATE) - i);
			Date beforeDate = now.getTime();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			dateList.add(format.format(beforeDate));
		}
		return dateList;
	}

	/**
	 * 获取当前传入日期的周一、周日两天的日期集合
	 *
	 * @param datetime
	 * @author 罗静
	 * @return
	 */
	public static List<String> getWeekDate(String datetime) {
		List<String> list = new ArrayList<String>();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(df.parse(datetime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期
		list.add(df.format(cal.getTime()));
		// 这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		// 增加一个星期，才是我们中国人理解的本周日的日期
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		list.add(df.format(cal.getTime()));
		return list;
	}

	/**
	 * 以当前时间为基准，按天进行偏移
	 * @param day 需要偏移的天数，负值为往后偏移，正值为往前偏移
	 * @return
	 */
	public static Date offsetByDay(int day) {
		Date dt = DateUtil.getDate();
        Calendar date = Calendar.getInstance();
        date.setTime(dt);
        date.add(Calendar.DAY_OF_MONTH, day);
        return date.getTime();
    }
	
	/**
	 * 取时间的按天进行偏移之后的时间
	 * @param dt 基准时间
	 * @param day 偏移的天数
	 * @return
	 */
    public static Date offsetByDay(Date dt , int day) {
        Calendar date = Calendar.getInstance();
        date.setTime(dt);
        date.add(Calendar.DAY_OF_MONTH, day);
        return date.getTime();
    }

    /**
     * 取时间的按天进行偏移之后的时间
     * @param date 基准时间
     * @param day 偏移的天数
     * @return
     */
    public static Date offsetByDay(String date , int day) {
        return offsetByDay(getDate(date), day);
    }


	/**
	 * 获取当前传入的日期、星期几、最近的上一个星期几来获取最近的上一个星期几所对应的日期
	 *
	 * @param datetime
	 * @param weekname1
	 *            时间参数格式：1 表示星期一
	 * @param weekname2
	 *            时间参数格式：2 表示最近的上一个星期二
	 * @author 牛逢路
	 * @return
	 */
	public static String getDateByWeekName(String datetime, int weekname1,
			int weekname2) {
		String lastDate = "";
		SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = dateFmt.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		if (weekname2 < weekname1) {
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)
					- (weekname1 - weekname2));
		} else {
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)
					- (7 - (weekname2 - weekname1)));
		}
		lastDate = dateFmt.format(cal.getTime());
		return lastDate;
	}

	/**
	 * 将UTC(Sun Jan 09 2011 00:00:00 GMT 0800)格式时间转换成yyyy-MM-dd
	 *
	 * @param utctime
	 *            utc格式的时间
	 * @return String yyyy-MM-dd格式时间
	 * @author 罗静
	 */
	public static String formatUTCToDate(String utctime) {
		String fmtDate = "";
		if (utctime == null || utctime.trim().length() == 0) {
			return fmtDate;
		}
		// 把utc的年取出来
		String year = "";
		SimpleDateFormat smtReturn = new SimpleDateFormat("MM-dd");
		;
		SimpleDateFormat sfmTurn = null;
		if (utctime.length() > 4) {
			if (utctime.indexOf("UTC") >= 0) {
				year = utctime.substring(utctime.length() - 4);
				sfmTurn = new SimpleDateFormat("EEE MMM dd HH:mm:ss", Locale.US);
			} else if (utctime.indexOf("GMT") >= 0) {
				year = utctime.substring(11, 15);
				sfmTurn = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss",
						Locale.US);
			} else {
				return utctime;
			}
		}
		// 先将utc时间转换成mm-dd
		try {
			Date date = sfmTurn.parse(utctime);
			String md = smtReturn.format(date);
			fmtDate = year + "-" + md;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return fmtDate;
	}

	/**
	 * 获取当前传入日期的前1天日期
	 *
	 * @param strdate
	 * @author 罗静
	 * @return
	 */
	public static String getBeforeDay(String strdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String reStr = "";
		try {
			Date dt = sdf.parse(strdate);
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(dt);
			rightNow.add(Calendar.DAY_OF_YEAR, -1);// 日期减1天
			Date dt1 = rightNow.getTime();
			reStr = sdf.format(dt1);// 昨天
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return reStr;
	}

	/**
	 * 获取当前传入日期的前一个月的日期
	 *
	 * @param datetime
	 * @author 罗静
	 * @return
	 */
	public static String getBeforeMonth(String strdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String reStr = "";
		try {
			Date dt = sdf.parse(strdate);
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(dt);
			rightNow.add(Calendar.MONTH, -1);// 日期减1个月
			Date dt1 = rightNow.getTime();
			reStr = sdf.format(dt1);// 昨天
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return reStr;
	}

	/**
	 * 获取当前日期下一天
	 * @return
	 */
	public static String getAfterDate() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date date = calendar.getTime();
		return sdf.format(date);
	}

	/**
	 * 日期推算
	 *
	 * @param year 将日期加上指定的年数
	 * @param month 将日期加上指定的月数
	 * @param day 将日期加上指定的天数
	 * @return
	 */
	public static Date getAfterDate(Date date, int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if(year != 0) calendar.add(Calendar.YEAR, year);
		if(month != 0) calendar.add(Calendar.MONTH, month);
		if(day != 0) calendar.add(Calendar.DAY_OF_YEAR, day);
		return calendar.getTime();
	}

	/**
	 * 时间推算
	 * @param date
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	public static Date getAfterDateTime(Date date, int year, int month, int day, int hour, int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, year);
		calendar.add(Calendar.MONTH, month);
		calendar.add(Calendar.DAY_OF_YEAR, day);
		// 时分秒
		calendar.add(Calendar.HOUR_OF_DAY, hour);
		calendar.add(Calendar.MINUTE, minute);
		calendar.add(Calendar.SECOND, second);
		return calendar.getTime();
	}

	/**
	 * 取日期时间对象
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	public static Date getDate(Integer year, Integer month, Integer date) {
		if(year == null) {
			year = 1970;
		}
		if(month == null) {
			month = 1;
		}
		if(date == null) {
			date = 1;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month-1, date, 0, 0, 0);
		return calendar.getTime();
	}



	/**
	 * 取今天开始时 yyyy-MM-dd 00:00:00
	 * @return
	 */
	public static String getTodayStart() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		return sdf.format(new Date());
	}
	/**
	 * 取今天结束时 yyyy-MM-dd 23:59:59
	 * @return
	 */
	public static String getTodayEnd() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		return sdf.format(new Date());
	}

	/**
	 * 比较两个时间的大小，若DATE1>=DATE2则true,否则false，
	 * @param DATE1
	 * @param DATE2
	 * @return
	 */
	public static boolean compareDate(Date date1, Date date2) {
		Calendar calendar= Calendar.getInstance();     //初始化日历对象
		calendar.setTime(date1);
		long timeOne=  calendar.getTimeInMillis();

		calendar.setTime(date2);
		long timeTwo=  calendar.getTimeInMillis();
		if(timeOne >= timeTwo){
			return true;
		}
	  return false;
	}

	/**
	 * 比较两个日期时间的大小
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareByDate(Date date1, Date date2) {
		String str1 = getStrDateTime(date1, "yyyy-MM-dd HH:mm:ss");
		String str2 = getStrDateTime(date2, "yyyy-MM-dd HH:mm:ss");
		return str1.compareTo(str2);
	}

	/**
	 * 比较两个日期的大小
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareByDay(Date date1, Date date2) {
		String str1 = getStrDateTime(date1, "yyyy-MM-dd");
		String str2 = getStrDateTime(date2, "yyyy-MM-dd");
		return str1.compareTo(str2);
	}

	/**
	 * 把某日期时间加上一些时分秒。现应用于跨日统计时间
	 * @param date
	 * @param hhMMss 格式 08:30:00
	 * @return
	 */
	public static Date addHHMMSS(Date date, String hhMMss) {
		if(hhMMss==null) return date;
		int delay_hour=0;
		int delay_minute=0;
		int delay_second=0;

		if(hhMMss!=null){
			try{
			String[] hms=hhMMss.split(":");
			delay_hour=Integer.parseInt(hms[0]);
			delay_minute=Integer.parseInt(hms[1]);
			delay_second=Integer.parseInt(hms[2]);
			}catch(Exception e){}
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, delay_hour);
		calendar.add(Calendar.MINUTE, delay_minute);
		calendar.add(Calendar.SECOND, delay_second);
		return calendar.getTime();
	}


    /**
     * 获取指定时间段内相差的年数
     * @param date1
     * @param date2
     * @return
     */
    public static int diffInYears(Date date1, Date date2) {
    	if(date1==null) {
    		date1 = new Date();
    	}
    	if(date2==null) {
    		date2 = date1;
    	}
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(date1);
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(date2);
        int c = (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR));
        return c;
    }


    /**
     * 获取指定时间段内相差的月份数
     * @param date1
     * @param date2
     * @return
     */
    public static int diffInMonths(Date date1, Date date2) {
    	if(date1==null) {
    		date1 = new Date();
    	}
    	if(date2==null) {
    		date2 = date1;
    	}
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(date1);
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(date2);
        int c = (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) * 12 + cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH);
        return c;
    }

	/**
	 * 获取指定时间段内相差的天数
	 * @param date1
	 * @param date2
	 * @return
	 * @throws Exception
	 */
	public static long diffInDays(Date date1, Date date2) {
    	if(date1==null) {
    		date1 = new Date();
    	}
    	if(date2==null) {
    		date2 = date1;
    	}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String str1 = getStrDateTime(date1, "yyyy-MM-dd");
		String str2 = getStrDateTime(date2, "yyyy-MM-dd");
		long to = 0;
		long from = 0;
		try {
			to = df.parse(str1).getTime();
			from = df.parse(str2).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long diff = (to - from) / (1000 * 60 * 60 * 24);
		return diff;
	}


    /**
     * 获取两个时间段内的秒数
     * date1 - date2
     * @param date1
     * @param date2
     * @return
     */
    public static long diffInSeconds(Date date1, Date date2) {
    	if(date1==null) {
    		date1 = new Date();
    	}
    	if(date2==null) {
    		date2 = date1;
    	}
        long d1 = date1 == null ? 0 : date1.getTime();
        long d2 = date2 == null ? 0 : date2.getTime();
        return (long) Math.ceil((d1 - d2) / (double) 1000);
    }
    /**
     * 获取两个时间段内的分钟数
     * @param date1
     * @param date2
     * @return
     */
    public static long diffInMinutes(Date date1, Date date2) {
        long d1 = date1 == null ? 0 : date1.getTime();
        long d2 = date2 == null ? 0 : date2.getTime();
        return (long) Math.ceil((d1 - d2) / (double) 60000);
    }


	/**
	 * 取某年某月的最后一天的日期
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getMonthLastDate(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// 不加下面2行，就是取当前时间前一个月的第一天及最后一天
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date lastDate = cal.getTime();
		return lastDate;
	}

    /**
     * 根据年、月获取对应月份天数
     * @param year
     * @param month
     * @return
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 取时间类型
     * @param start_time hh:MM:ss
     * @return
     */
	public static Date getTime(String start_time) {
		String str = "1970-01-01 " + start_time;
		return DateUtil.getDatetime(str);
	}
}
