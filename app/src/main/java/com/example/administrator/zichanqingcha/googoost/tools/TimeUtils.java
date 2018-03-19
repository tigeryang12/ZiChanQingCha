package com.example.administrator.zichanqingcha.googoost.tools;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * DATE 继承于 java.util.Date，一个强大的日期工具类。
 */

@SuppressLint("SimpleDateFormat")
public class TimeUtils extends Date {

	private static final long serialVersionUID = 2155545266875552658L;

	private static TimeUtils calendar;

	private static SimpleDateFormat second = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
	private static SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat detailDay = new SimpleDateFormat("yyyy年MM月dd日");
	private static SimpleDateFormat fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	private static SimpleDateFormat tempTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat excelDate = new SimpleDateFormat("yyyy/MM/dd");

	/**
	 * 单例构造
	 *
	 * @return
	 */
	public static TimeUtils getInatance() {
		if (calendar == null)
			calendar = new TimeUtils();
		return calendar;
	}

	private TimeUtils() {
	}

	/**
	 * 格式化excel中的时间
	 * 
	 * @return
	 */
	public String formatDateForExcelDate(Date date) {
		return excelDate.format(date);
	}

	/**
	 * 以毫秒为单位获取当前时间
	 * 
	 * @return
	 */
	public  long getCurrentTimeInLong() {
		return System.currentTimeMillis();
	}

	/**
	 * 毫秒转string
	 * 
	 * @return
	 */
	private static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
		return dateFormat.format(new Date(timeInMillis));
	}

	/**
	 * 以毫秒为单位获取当前时间格式化方式 返回固定格式 yy-MM-dd hh:mm:ss
	 * 
	 * @return
	 */
	public String getCurrentTimeInString() {
		return getTime(getCurrentTimeInLong());
	}

	/**
	 * 以毫秒为单位获取当前时间格式化方式 返回固定格式 yy-MM-dd hh:mm:ss
	 * 
	 * @return
	 */
	public String getCurrentTimeInString(SimpleDateFormat dateFormat) {
		return getTime(getCurrentTimeInLong(), dateFormat);
	}

	/**
	 * 毫秒转string 固定格式 yy-MM-dd hh:mm:ss
	 * 
	 * @param timeInMillis
	 * @return
	 */
	private static String getTime(long timeInMillis) {
		return getTime(timeInMillis, second);
	}

	/**
	 * 将日期格式化作为文件名 yyyy-MM-dd-HH-mm-ss
	 * 
	 * @return
	 */
	public String formatDateForFileName(Date date) {
		return fileName.format(date);
	}

	/**
	 * 格式化日期(精确到秒) yy-MM-dd hh:mm:ss
	 * 
	 * @return
	 */
	public String formatDateSecond12(Date date) {
		return second.format(date);
	}

	/**
	 * 格式化日期(精确到秒) yyyy-MM-dd HH:mm:ss
	 * 
	 * @return String
	 */
	public String formatDateSecond24(Date date) {
		return tempTime.format(date);
	}

	/**
	 * 格式化日期(精确到秒) yyyy-MM-dd HH:mm:ss
	 * 
	 * @return Date
	 */
	public Date formatDateSecond24(String str) {
		try {
			return tempTime.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	/**
	 * 格式化日期(精确到天) yyyy-MM-dd
	 * 
	 * @return
	 */
	public String formatDateDay(Date date) {
		return day.format(date);
	}

	/**
	 * 格式化日期(精确到天) yyyy年MM月dd日
	 * 
	 * @return
	 */
	public String formatDateDetailDay(Date date) {
		return detailDay.format(date);
	}
	
	/**
	 * 将日期按照一定的格式进行格式化为字符串。<br/>
	 * 例如想将时间格式化为2012-03-05 12:56 ,则只需要传入formate为yyyy-MM-dd HH:mm即可。
	 * 
	 * @return String 格式后的日期字符串。如果当前对象为null，则直接返回null。
	 */
	public String formatDateInAnyThing(String formate) {
		DateFormat df = new SimpleDateFormat(formate);
		return (null == this) ? null : df.format(this);
	}

	/**
	 * 将字符串转换成日期
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public Date parseStringToDate(String date) throws Exception {
		return day.parse(date);
	}

	/**
	 * 功能：转换为Calendar。
	 * 
	 * @return Calendar
	 */
	public Calendar toCalendar() {
		Calendar c = Calendar.getInstance();
		c.setTime(this);
		return c;
	}

	/**
	 * 功能：判断日期是否和当前date对象在同一天。 date 比较的日期
	 * 
	 * @return boolean 如果在返回true，否则返回false。
	 */
	public boolean isSameDay(TimeUtils date) {
		if (date == null) {
			throw new IllegalArgumentException("日期不能为null");
		}
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date);
		return this.isSameDay(cal2);
	}

	/**
	 * 功能：判断日期是否和当前date对象在同一天。 cal 比较的日期
	 * 
	 * @return boolean 如果在返回true，否则返回false。
	 */
	public boolean isSameDay(Calendar cal) {
		if (cal == null) {
			throw new IllegalArgumentException("日期不能为null");
		}
		// 当前date对象的时间
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(this);
		return (cal1.get(Calendar.ERA) == cal.get(Calendar.ERA)
				&& cal1.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && cal1
					.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR));
	}

	/**
	 * 功能：得到当月有多少天。
	 * 
	 * @return int
	 */
	public int daysNumOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(this);
		return cal.getActualMaximum(Calendar.DATE);
	}

	/**
	 * 得到秒。格式：56<br/>
	 * 
	 * @return int
	 */
	public int getSecondInt() {
		return Integer.parseInt(formatDateInAnyThing("ss"));
	}

	/**
	 * 得到分钟。格式：56<br/>
	 * 
	 * @return int
	 */
	public int getMinuteInt() {
		return Integer.parseInt(formatDateInAnyThing("mm"));
	}

	/**
	 * 得到小时。格式：23<br/>
	 * 
	 * @return int
	 */
	public int getHourInt() {
		return Integer.parseInt(formatDateInAnyThing("HH"));
	}

	/**
	 * 得到日。格式：26<br/>
	 * 注意：这里1日返回1,2日返回2。
	 * 
	 * @return int
	 */
	public int getDayInt() {
		return Integer.parseInt(formatDateInAnyThing("dd"));
	}

	/**
	 * 得到月。格式：5<br/>
	 * 注意：这里1月返回1,2月返回2。
	 * 
	 * @return int
	 */
	public int getMonthInt() {
		return Integer.parseInt(formatDateInAnyThing("MM"));
	}

	/**
	 * 得到年。格式：2013
	 * 
	 * @return int
	 */
	public int getYearInt() {
		return Integer.parseInt(formatDateInAnyThing("yyyy"));
	}

	/**
	 * 得到短时间。格式：12:01
	 * 
	 * @return String
	 */
	public String getShortTime() {
		return formatDateInAnyThing("HH:mm");
	}

	/**
	 * 得到长时间。格式：12:01:01
	 * 
	 * @return String
	 */
	public String getLongTime() {
		return formatDateInAnyThing("HH:mm:ss");
	}

	/**
	 * 根据日期得到星期几,得到数字。<br/>
	 * 7, 1, 2, 3, 4, 5, 6
	 * 
	 * @return Integer 如：6
	 */
	public int getDayOfWeekInt() {
		Integer dayNames[] = { 7, 1, 2, 3, 4, 5, 6 };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek < 0)
			dayOfWeek = 0;
		return dayNames[dayOfWeek];
	}



}