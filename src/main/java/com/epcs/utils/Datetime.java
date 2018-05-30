package com.epcs.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Datetime {

	public static String DATETIME_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";
	public static String DATETIME_FORMAT_YMDHM = "yyyy-MM-dd HH:mm";
	public static String DATETIME_FORMAT_YMD = "yyyy-MM-dd";
	public static String DATETIME_FORMAT_HM = "HH:mm";
	private static String folder = "";

	public static Timestamp getTimeStampFromString(String timeStr, String format) {

		if (timeStr != null && timeStr.length() != 0) {
			SimpleDateFormat ft = new SimpleDateFormat(format);
			try {
				return new java.sql.Timestamp((ft.parse(timeStr)).getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return new java.sql.Timestamp(new Date().getTime());
	}

	public static Date parse(String timeStr, String format) {

		if (timeStr != null && timeStr.length() != 0) {
			SimpleDateFormat ft = new SimpleDateFormat(format);
			try {
				return ft.parse(timeStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	public static String getTimeStampStringYMDHM(Timestamp ts) {

		if (ts != null) {
			SimpleDateFormat ft = new SimpleDateFormat(DATETIME_FORMAT_YMDHM);
			return ft.format(ts);
		}

		return null;
	}

	public static String getTimeStampStringYMD(Timestamp ts) {

		if (ts != null) {
			SimpleDateFormat ft = new SimpleDateFormat(DATETIME_FORMAT_YMD);
			return ft.format(ts);
		}

		return null;
	}

	public static String getTimeStampStringHM(Timestamp ts) {

		if (ts != null) {
			SimpleDateFormat ft = new SimpleDateFormat(DATETIME_FORMAT_HM);
			return ft.format(ts);
		}

		return null;
	}

	public static String getTimeStampString(Timestamp ts) {

		if (ts != null) {
			SimpleDateFormat ft = new SimpleDateFormat(DATETIME_FORMAT_FULL);
			return ft.format(ts);
		}

		return null;
	}

	public static String getTimeStampString(Timestamp ts, String format) {

		if (ts != null) {
			SimpleDateFormat ft = new SimpleDateFormat(format);
			return ft.format(ts);
		}

		return null;
	}

	public static Timestamp getTimeStampFromStringFull(String timeStr) {

		if (timeStr != null && timeStr.length() != 0) {
			SimpleDateFormat ft = new SimpleDateFormat(DATETIME_FORMAT_FULL);
			try {
				System.out.print("dxxx");
				return new java.sql.Timestamp((ft.parse(timeStr)).getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	public static Timestamp getTimeStampFromString(String timeStr) {

		if (timeStr != null && timeStr.length() != 0) {
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return new Timestamp((ft.parse(timeStr)).getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	public static Timestamp getTimeStampNow() {
		return new java.sql.Timestamp(new Date().getTime());
	}

	public static Long getTimeNow() {
		return new Date().getTime();
	}

	public static String getDate() {
		Date dt = new Date();
		DATETIME_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT_FULL);
		return format.format(dt);
	}

	public static String getDate(Date date, String targetFormat) {
		if (date == null)
			return null;
		SimpleDateFormat format = new SimpleDateFormat(targetFormat);
		return format.format(date);
	}

	public static String getDateNow(String targetFormat) {
		SimpleDateFormat format = new SimpleDateFormat(targetFormat);
		return format.format(new Date());
	}

	public static String getShotDate() {
		Date dt = new Date();
		DATETIME_FORMAT_FULL = "yyyy-MM-dd";
		SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT_FULL);
		return format.format(dt);
	}

	public static String getTimeStamp() {
		Date dt = new Date();
		DATETIME_FORMAT_FULL = "yyyyMMddHHmmssSSS";
		SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT_FULL);
		return format.format(dt);
	}

	public static Timestamp getTimeStampEndOfDay(Timestamp ts) {

		/* other way */
		SimpleDateFormat formater = new SimpleDateFormat(DATETIME_FORMAT_YMD);
		SimpleDateFormat formater2 = new SimpleDateFormat(DATETIME_FORMAT_FULL);
		Date start;
		try {
			start = formater2.parse(formater.format(ts) + " 23:59:59");
			return new java.sql.Timestamp(start.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}
	public static Timestamp getTimeStampFirstOfDay(Timestamp ts) {

		/* other way */
		SimpleDateFormat formater = new SimpleDateFormat(DATETIME_FORMAT_YMD);
		SimpleDateFormat formater2 = new SimpleDateFormat(DATETIME_FORMAT_FULL);
		Date start;
		try {
			start = formater2.parse(formater.format(ts) + " 00:00:00");
			return new java.sql.Timestamp(start.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}
	public static String getYearDate() {
		Date dt = new Date();
		DATETIME_FORMAT_FULL = "yy";
		SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT_FULL);
		folder = format.format(dt);
		DATETIME_FORMAT_FULL = "MM";
		format = new SimpleDateFormat(DATETIME_FORMAT_FULL);
		folder = folder + "/" + format.format(dt);
		DATETIME_FORMAT_FULL = "dd";
		format = new SimpleDateFormat(DATETIME_FORMAT_FULL);
		folder = folder + "/" + format.format(dt);
		return folder;
	}

	public static long getDaysBetween(String dateStart, String dateEnd) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date d1;
		Date d2;
		long diff = 0;
		try {
			d1 = format.parse(dateStart);

			d2 = format.parse(dateEnd);

			diff = (d2.getTime() - d1.getTime()) / (24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return diff;
	}

	public static long getDaysBetween(Timestamp dateStart, Timestamp dateEnd) {

		if (dateEnd.after(dateStart))
			return (dateEnd.getTime() - dateStart.getTime()) / (24 * 60 * 60 * 1000);
		else
			return -(dateStart.getTime() - dateEnd.getTime()) / (24 * 60 * 60 * 1000);
	}

	public static int getDaysBetween(java.util.Calendar d1, java.util.Calendar d2) {
		if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
			java.util.Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		int days = d2.get(java.util.Calendar.DAY_OF_YEAR) - d1.get(java.util.Calendar.DAY_OF_YEAR);
		int y2 = d2.get(java.util.Calendar.YEAR);
		if (d1.get(java.util.Calendar.YEAR) != y2) {
			d1 = (java.util.Calendar) d1.clone();
			do {
				days += d1.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
				d1.add(java.util.Calendar.YEAR, 1);
			} while (d1.get(java.util.Calendar.YEAR) != y2);
		}
		return days;
	}

	public static Timestamp getDateForTimestamp(Timestamp st) {

		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

		try {
			return new java.sql.Timestamp((ft.parse(ft.format(st))).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return st;
		}

	}

	public static long getDaysBetweenWithoutTime(Timestamp dateStart, Timestamp dateEnd) {
		Timestamp start = getDateForTimestamp(dateStart);
		Timestamp end = getDateForTimestamp(dateEnd);

		if (end.after(start))
			return (end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000);
		else
			return -(start.getTime() - end.getTime()) / (24 * 60 * 60 * 1000);
	}

	/**
	 * Days operate days<0 -> decline days > 0 increase
	 */
	public static Timestamp addDays(Timestamp ts, int days) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(ts);
		ca.add(Calendar.DATE, days);
		return new Timestamp(ca.getTime().getTime());
	}

	public static Timestamp addOneDay(Timestamp ts) {
		return addDays(ts, 1);
	}

	public static Timestamp addOneWeek(Timestamp ts) {
		return addDays(ts, 7);
	}

	/**
	 * Month operate days<0 -> decline days > 0 increase
	 */
	public static Timestamp addMonthes(Timestamp ts, int monthes) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(ts);

		ca.add(Calendar.MONTH, monthes);

		return new Timestamp(ca.getTime().getTime());
	}

	public static Timestamp addOneMonth(Timestamp ts) {
		return addMonthes(ts, 1);
	}

	public static Timestamp minusOneMonth(Timestamp ts) {
		return addMonthes(ts, -1);
	}

	/**
	 * Years operate Years<0 -> decline Years > 0 increase
	 */
	public static Timestamp addYears(Timestamp ts, int years) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(ts);
		ca.add(Calendar.YEAR, years);
		return new Timestamp(ca.getTime().getTime());
	}

	public static Timestamp addOneYear(Timestamp ts) {
		return addYears(ts, 1);
	}

	public static Timestamp minusOneYear(Timestamp ts) {
		return addYears(ts, -1);
	}

	/**
	 * Get pre month first day and last day example: 2012-01-1 will return:
	 * [2011-12-31 00:00:00.0, 2011-12-01 00:00:00.0]
	 **/
	public static List<Timestamp> getPreMonthFirstAndLastDay(Timestamp ts) {
		List<Timestamp> list = new ArrayList<Timestamp>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(ts);

		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date lastDate = cal.getTime();
		ts = new Timestamp(lastDate.getTime());
		list.add(ts);

		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date firstDate = cal.getTime();
		ts = new Timestamp(firstDate.getTime());
		list.add(ts);

		return list;
	}

	/**
	 * 得到几天前的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Timestamp getDateBefore(Timestamp d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return new Timestamp(now.getTime().getTime());
	}

	/**
	 * 得到几天后的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Timestamp getDateAfter(Timestamp d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return new Timestamp(now.getTime().getTime());
	}

	/**
	 * Get current month first day example: 2012-01-3 will return: [2012-01-01
	 * 00:00:00.0]
	 **/
	public static Timestamp getMonthFirstDay(Timestamp ts) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(ts);

		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date firstDate = cal.getTime();
		return new Timestamp(firstDate.getTime());
	}
	public static Timestamp getFirstDay(Timestamp ts) {

			Calendar cal = Calendar.getInstance();
		cal.setTime(ts);

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
			Date firstDate = cal.getTime();
		return new Timestamp(firstDate.getTime());
	}
	/**
	 * Get current  first day example: 2012-01-3 will return: [2012-01-01
	 * 00:00:00.0]
	 **/

	/**
	 * Get current week first day example: 2017-07-27 will return: [2017-07-23
	 * 00:00:00.0]
	 **/
	public static Timestamp getWeekFirstDay(Timestamp ts) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(ts);

		cal.set(Calendar.DAY_OF_WEEK, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date firstDate = cal.getTime();
		return new Timestamp(firstDate.getTime());
	}

	/**
	 * Get current year first day example: 2017-07-27 will return: [2017-01-21
	 * 00:00:00.0]
	 **/
	public static Timestamp getYearFirstDay(Timestamp ts) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(ts);

		cal.set(Calendar.DAY_OF_YEAR, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date firstDate = cal.getTime();
		return new Timestamp(firstDate.getTime());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Timestamp tsStart = Datetime.getTimeStampFromString("2017-02-20 12:55:00", "yyyy-MM-dd");

		Timestamp tsEnd1 = Datetime.getTimeStampFromString("2017-02-22", "yyyy-MM-dd");

		Timestamp tsEnd2 = Datetime.getTimeStampFromString("2012-01-16", "yyyy-MM-dd");

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		//
		// Timestamp stStr =
		// Datetime.getTimeStampFromString(getDateForTimestamp(Datetime.getTimeStampNow()),
		// "yyyy-MM-dd");
		//
		// String str2 = format2.format(stStr);
		Timestamp tst = getTimeStampNow();
		System.out.println(getTimeStampString(getTimeStampNow()));
		System.out.println(getTimeStampString(minusOneYear(tst)));
		System.out.println(getTimeStampString(minusOneYear(minusOneYear(tst))));
		System.out.println(getTimeStampString(addOneWeek(addOneWeek(addOneWeek(addOneWeek(tst))))));
		System.out.println(Datetime.getFirstDay(Datetime.getTimeStampNow()));
		// ts = minusOneMonth(ts);
		// getPreMonthFirstAndLastDay(ts);
		//
		// ts = minusOneMonth(ts);
		// getPreMonthFirstAndLastDay(ts);
		//
		// ts = minusOneMonth(ts);
		// getPreMonthFirstAndLastDay(ts);
		//
		// ts = minusOneMonth(ts);
		// getPreMonthFirstAndLastDay(ts);
		//
		// ts = minusOneMonth(ts);
		// getPreMonthFirstAndLastDay(ts);
		//
		// ts = minusOneMonth(ts);
		// getPreMonthFirstAndLastDay(ts);

	}
}