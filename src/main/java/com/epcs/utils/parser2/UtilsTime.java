package com.epcs.utils.parser2;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class UtilsTime {
    // kk24 hh12
    public static final DateFormat FULL_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat FORMAT1 = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    public static String getDate(long time) {
        Date date = new Date(time);
        return FULL_FORMAT.format(date);
    }

    public static String getTimestampString(long time) {
        Date date = new Date(time);
        return TIMESTAMP_FORMAT.format(date);
    }

    public static String getBcdString(long time) {
        Date date = new Date(time);
        return new SimpleDateFormat("yyMMddkkmmss").format(date);
    }

    public static Timestamp string2Timestamp(String timestampString) {

        // Timestamp ts = new Timestamp(System.currentTimeMillis());
        // String tsStr = "2011-05-09 11:49:45";
        Timestamp timestamp = null;
        try {
            timestamp = Timestamp.valueOf(timestampString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    public static String timestamp2String(Timestamp timestamp) {
        String timestampString = "";
        try {
            timestampString = FULL_FORMAT.format(timestamp);
            // timestampString = timestamp.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestampString;
    }

    public static long timestamp2Long(String timestampString) {

        // Timestamp ts = new Timestamp(System.currentTimeMillis());
        // String tsStr = "2011-05-09 11:49:45";
        long time = 0;
        try {
            Timestamp timestamp = Timestamp.valueOf(timestampString);
            time = timestamp.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static long split2TimeLong(int year, int month, int day, int hour, int min, int sec) {
        long time = 0;

        try {
            String str = String.format("%04d%02d%02d%02d%02d%02d", year, month, day, hour, min,
                    sec);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
            time = sdf.parse(str).getTime();
        } catch (Exception e) {

        }
        return time;
    }

    public static boolean isExpired(String timestampString) {
        // must be format like "2015-11-15 11:05:00";
        Timestamp timestamp = null;
        boolean isExpired = false;

        try {
            timestamp = Timestamp.valueOf(timestampString);
            isExpired = System.currentTimeMillis() > timestamp.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExpired;
    }

    final static long DAYS_MS = 24 * 60 * 60 * 1000;

    public static long expireDays(String expiredTimeString) {
        try {
            Timestamp timestamp = Timestamp.valueOf(expiredTimeString);
            long expiredTime = timestamp.getTime() - System.currentTimeMillis();
            // timestampString = timestamp.toString();
            return expiredTime / DAYS_MS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long bcdTime2Time(String bcdTime) {
        if (bcdTime == null || bcdTime.length() != 12)
            return 0;

        long time = 0;

        try {
            String str = "20" + bcdTime;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
            time = sdf.parse(str).getTime();
        } catch (Exception e) {

        }
        return time;
    }

    public static Timestamp bcdTime2TimeStamp(String bcdTime) {
        if (bcdTime == null || bcdTime.length() != 12)
            return null;

        Timestamp timeStamp = null;

        try {
            String str = "20" + bcdTime;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
            long time = sdf.parse(str).getTime();
            timeStamp = Timestamp.valueOf(getTimestampString(time));
        } catch (Exception e) {

        }
        return timeStamp;
    }
}
