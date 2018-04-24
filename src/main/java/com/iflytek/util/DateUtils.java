package com.iflytek.util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间装换工具
 * @author yu
 */
public class DateUtils {


    public static final String YYYYDDHHMMSS = "yyyyMMddHHmmss";


    /**
     * 将date格式化为指定格式的字符串时间
     *
     * @param date java.util.Date
     * @param format 需要格式的样式(yyyy-MM-dd等)
     * @return String
     */
    public static String date2Str(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINESE);
        if(null == date){
            throw new NullPointerException("the param of data can't be null");
        }
        if(null == format|| "".equals(format)){
            throw new NullPointerException("the param of format can't be null or empty");
        }
        return sdf.format(date);
    }




}
