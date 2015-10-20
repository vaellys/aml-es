package com.ist.common.es.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtils {
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat SHORT_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat DATE_TIME_FORMAT_T = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private DateUtils() {
    }

    /**
     * 取得当前系统时间 yyyy-MM-dd HH:mm:ss
     * 
     * @return String
     */
    public static String getCurrTime() {
        java.util.Date dateTime = new java.util.Date();
        return DATE_TIME_FORMAT.format(dateTime);
    }

    /**
     * 格式化时间缀 yyyy-MM-dd HH:mm:ss
     * 
     * @param timestamp
     * @return String
     */
    public static String format(Timestamp timestamp) {
        return DATE_TIME_FORMAT.format(timestamp);
    }

    /**
     * 格式化日期为 yyyy-MM-dd
     * 
     * @param date
     * @return String
     */
    public static String format(Date date) {
        return DATE_FORMAT.format(date);
    }

    /**
     * 格式化时间 HH:mm:ss
     * 
     * @param time
     * @return String
     */
    public static String format(Time time) {
        return TIME_FORMAT.format(time);
    }

    /**
     * 将字符串解析为时间缀
     * 
     * @param timestamp
     * @return Timestamp
     * @throws ParseException
     */
    public static Timestamp parseTimestamp(String timestamp) throws ParseException {
        try {
            return new Timestamp(DATE_TIME_FORMAT.parse(timestamp).getTime());
        } catch (ParseException e) {
            return new Timestamp(SHORT_DATE_TIME_FORMAT.parse(timestamp).getTime());
        }
    }

    /**
     * 将日期字符串解析为日期类型
     * 
     * @param timestamp
     * @return Date
     * @throws ParseException
     */
    public static Date parseDate(String timestamp) throws ParseException {
        return new Date(DATE_FORMAT.parse(timestamp).getTime());
    }
    
    /**
     * 格式化日期为 yyyy-MM-ddTHH:mm:ss
     * 
     * @param date
     * @return String
     */
    public static String formatT(Date date) {
        return DATE_TIME_FORMAT_T.format(date);
    }
    
    public static void main(String[] args) {
        Date d = new Date(System.currentTimeMillis());
        System.out.println(formatT(d));
    }
}
