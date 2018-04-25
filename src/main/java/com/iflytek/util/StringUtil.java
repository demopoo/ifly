package com.iflytek.util;

public class StringUtil {

    public static String[] split(String str,String regex){
        if(null != str){
            return str.split(regex);
        }
        return null;
    }
}
