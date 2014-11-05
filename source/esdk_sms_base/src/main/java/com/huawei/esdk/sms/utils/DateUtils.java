package com.huawei.esdk.sms.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class DateUtils
{
    public static final String DT_FT_YYYYMMDDHHMISSSSS = "yyyyMMddHHmmssSSS";
    
    public static final String DT_FT_YYYYMMDDHHMISS = "yyyyMMddHHmmss";
    
    public static final String DT_FT_YYYYMMDDHHMI = "yyyyMMddHHmm";
    
    public static final String DT_FT_YYYYMMDDHH = "yyyy-MM-dd HH";
    
    public static final String DT_FT_YYYYMMDD = "yyyyMMdd";
    
    public static String formatDate(String formatPatter)
    {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(formatPatter);
        return sdf.format(currentDate);
    }
    
    public static String formatDate(Date date, String formatPatter)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(formatPatter);
        return sdf.format(date);
    }
    
    public static Date getCurrentDateTime()
    {
        return Calendar.getInstance().getTime();
    }
    
    /**
     * 获取当前日期以默认格式(yyyyMMddHHmmssSSS)的字符串
     * 
     * @return 当前日期的字符串
     */
    public static String getCurrentDateAsString(String format)
    {
        return formatDate(getCurrentDateTime(), format);
    }
}
