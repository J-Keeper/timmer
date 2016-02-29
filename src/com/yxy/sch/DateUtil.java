package com.yxy.sch;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间日期通用工具类
 * 
 * @author YXY
 * @date 2015年8月27日 上午9:54:24
 */
public class DateUtil {
	static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 将Timetamp型数据转换为形如:2015年8月27日23:10:00 的中文表示
	 * 
	 * @param time
	 * @return String
	 * @throws
	 * @author YXY
	 */
	public static String ttDateToChinese(Timestamp time) {
		if (time == null) {
			return null;
		}
		return dateToChinese(new Date(time.getTime()));
	}

	/**
	 * Timetamp型数据转换为形如:2015年8月27日23:10:00 的中文表示
	 * 
	 * @param date
	 * @return String
	 * @throws
	 * @author YXY
	 */
	public static String dateToChinese(Date date) {
		String bt = df.format(date);
		String[] btimes = bt.split(" ")[0].split("-");
		StringBuffer sb = new StringBuffer();
		sb.append(btimes[0]).append("年");
		sb.append(btimes[1]).append("月");
		sb.append(btimes[2]).append("日");
		sb.append(bt.split(" ")[1]);
		return sb.toString();
	}

	/**
	 * 将Timestap型日期转换为yyyy-MM-dd HH:mm:ss格式
	 * 
	 * @param time
	 * @return String
	 * @throws
	 * @author YXY
	 */
	public static String formateDate(Timestamp time) {
		if (time == null) {
			return null;
		}
		return df.format(new Date(time.getTime()));
	}

	/**
	 * 判断某个日期在指定的日期内(包含边界)
	 * 
	 * @param date
	 * @param start
	 * @param end
	 * @return boolean
	 * @throws
	 * @author YXY
	 */
	public static boolean isBetweenTime(Date date, Date begin, Date end) {
		if (date == null) {
			return false;
		}
		if (begin == null && end == null) {
			return false;
		}
		return begin.getTime() <= date.getTime()
				&& date.getTime() <= end.getTime();
	}

	/**
	 * 将形如 yyyy-MM-dd HH:mm:ss的字符串转换为现有java.util.Date
	 * 
	 * @return Date
	 * @throws
	 * @author YXY
	 */
	public static Date getDate(String express) {
		Date date = null;
		try {
			df.parse(express);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 将形如HH:mm:ss转化为今天的时间(24小时制)
	 * 
	 * @param express
	 * @return Date
	 * @throws
	 * @author YXY
	 */
	public static Date getTodayDate(String express) {
		Calendar calendar = Calendar.getInstance();
		String[] args = express.split(":");
		int hh = Integer.parseInt(args[0]);
		int mm = Integer.parseInt(args[1]);
		int ss = Integer.parseInt(args[2]);

		calendar.set(Calendar.HOUR_OF_DAY, hh);
		calendar.set(Calendar.MINUTE, mm);
		calendar.set(Calendar.SECOND, ss);

		return calendar.getTime();
	}
}
