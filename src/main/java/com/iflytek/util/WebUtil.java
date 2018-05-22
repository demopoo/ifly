package com.iflytek.util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author yu
 */
public class WebUtil {

    /**
     * 获取流
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getStream(HttpServletRequest request) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "GBK"));
        StringBuffer ReString = new StringBuffer();
        String tmp = "";
        while (true) {
            tmp = br.readLine();
            if (tmp == null) {
                break;
            } else {
                ReString.append(tmp);
            }
        }
        return ReString.toString();
    }
}
