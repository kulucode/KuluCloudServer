package com.kulu.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static final String FORMAT_DATE = "yyyy-MM-dd";
	public static final String FORMAT_TIME = "HH:mm:ss";
	public static final String FORMAT_DATETIME = FORMAT_DATE + " " + FORMAT_TIME;

	// region 日期时间转换为某天时间的开始和时间的结束
	// 获得今天的开始时时间
	public static Date GetNowDayStart() {
		return SetDayTimeStart(new Date());
	}

	// 获得今天的结束时的时间
	public static Date GetNowDayEnd() {
		return SetDayTimeEnd(new Date());
	}

	// 将 d 的时间部分设置为一天的开始
	public static Date SetDayTimeStart(Date d) {
		if (d == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	// 将 d 的时间部分设置为一天的结束
	public static Date SetDayTimeEnd(Date d) {
		if (d == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}
	// endregion

	// // 日期距离现在过去了多少天
	// public static long DayIsNowAfterDay(Date 天) {
	// long m = new Date().getTime() - 天.getTime();
	// return TypeConvert.ToLong(Math.floor(m / 86400000.0));
	// }

	// region 日期的之前之后之间判断

	// 天 的日期 在不在 今天 之后, 包括等于
	public static boolean DayHasNowAfter(Date 天) {
		return DayHasAfter(天, new Date());
	}

	// 天 的日期 在不在 今天 之前, 包括等于
	public static boolean DayHasNowBefore(Date 天) {
		return DayHasBefore(天, new Date());
	}

	// 天 的日期 在不在 开始的日期 之后, 包括等于
	public static boolean DayHasAfter(Date 天, Date 开始) {
		return HasAfter(SetDayTimeStart(天), SetDayTimeStart(开始));
	}

	// 天 的日期 在不在 结束的日期 之前, 包括等于
	public static boolean DayHasBefore(Date 天, Date 结束) {
		return HasBefore(SetDayTimeEnd(天), SetDayTimeEnd(结束));
	}

	// 天 的 日期 在不在 开始的日期 与 结束的日期 之间, 包括等于
	public static boolean DayHasBetween(long 天, long 开始, long 结束) {
		return DayHasBetween(new Date(天), new Date(开始), new Date(结束));
	}

	public static boolean DayHasBetween(Date 天, Date 开始, Date 结束) {
		return DayHasAfter(天, 开始) && DayHasBefore(天, 结束);
	}
	// endregion

	// region 日期时间的之前之后之间判断
	// 时间 在不在 开始 之后, 包括等于
	public static boolean HasAfter(Date 时间, Date 开始) {
		if (时间 == null || 开始 == null)
			return false;
		return 开始.getTime() <= 时间.getTime();
	}

	// 时间 在不在 结束 之前, 包括等于
	public static boolean HasBefore(Date 时间, Date 结束) {
		if (时间 == null || 结束 == null)
			return false;
		return 结束.getTime() >= 时间.getTime();
	}

	// 时间 在不在 现在 之前, 包括等于
	public static boolean HasNowBefore(Date 时间) {
		return HasBefore(时间, new Date());
	}

	// 时间 在不在 开始 与 结束 之间, 包括等于
	public static boolean HasBetween(Date 时间, Date 开始, Date 结束) {
		return HasAfter(时间, 开始) && HasBefore(时间, 结束);
	}
	// endregion

	// 使 D 增加 day 天
	public static Date AddDay(Date d, int day) {
		return DateUtil.Add(d, Calendar.DATE, day);
	}

	// 使 D 增加 week 周
	public static Date AddWeek(Date d, int week) {
		return DateUtil.Add(d, Calendar.WEEK_OF_YEAR, week);
	}

	public static Date Add(Date d, int type, int num) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(type, num);
		return c.getTime();
	}

	public static Date Set(Date d, int type, int num) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(type, num);
		return c.getTime();
	}

	// 获取一个只有时间的Date
	public static Date NewTime(int hou, int min) {
		Calendar c = Calendar.getInstance();
		c.set(1970, 0, 1, hou, min, 0);
		return c.getTime();
	}

	// 获取一个只有日期的Date
	public static Date NewDate(int year, int month, int date) {
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, date, 0, 0, 0);
		return c.getTime();
	}

	// 获取指定日期时间的Date
	public static Date NewDateTime(int year, int month, int date, int hou, int min) {
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, date, hou, min, 0);
		return c.getTime();
	}

	public static String GetWeekString(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int weekDay = c.get(Calendar.DAY_OF_WEEK);
		if (Calendar.MONDAY == weekDay) {
			return "星期一";
		}
		if (Calendar.TUESDAY == weekDay) {
			return "星期二";
		}
		if (Calendar.WEDNESDAY == weekDay) {
			return "星期三";
		}
		if (Calendar.THURSDAY == weekDay) {
			return "星期四";
		}
		if (Calendar.FRIDAY == weekDay) {
			return "星期五";
		}
		if (Calendar.SATURDAY == weekDay) {
			return "星期六";
		}
		if (Calendar.SUNDAY == weekDay) {
			return "星期日";
		}
		return "";
	}

	public static String ToDateString(Date value) {
		return ToString(value, FORMAT_DATE);
	}

	public static String ToTimeString(Date value) {
		return ToString(value, FORMAT_TIME);
	}

	public static String ToDateTimeString(Date value) {
		return ToString(value, FORMAT_DATETIME);
	}

	// public static String GetTimestamp() {
	// return ToTimestamp(new Date());
	// }
	//
	// public static String ToTimestamp(Date value) {
	// return ToLongTimestamp(value).substring(0, 10);
	// }
	//
	// public static String GetLongTimestamp() {
	// return ToLongTimestamp(new Date());
	// }

	// public static String ToLongTimestamp(Date value) {
	// String time = TypeConvert.ToString(value.getTime());
	// return time;
	// }

	/**
	 * 目标时间距离现在过去了多少毫秒
	 *
	 * @param value
	 * @return
	 */
	public static long BeforeTimestamp(Date value) {
		return System.currentTimeMillis() - value.getTime();
	}

	public static String ToString(Date value, String format) {
		if (value == null) {
			value = new Date();
		}
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(value);
	}

}
