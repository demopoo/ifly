package com.iflytek.service.impl;

import com.iflytek.entity.Constant;
import com.iflytek.service.CacheService;
import com.iflytek.util.ConfigUtils;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存服务实现类
 * @author jjliu15@iflytek.com
 * @date 2018/1/22
 */

@Slf4j
@Service
public class CacheServiceImpl implements CacheService {

    @Value("${application.quorum}")
    private String quorum;
    /**
     * 集群列表
     */
    private List<String> urlList;

    @Override
    @CacheEvict(value = "resCache",allEntries = true)
    public String clearCache(String all) {
        String res = Constant.STRING_SUCCESS_MSG;
        //如果参数不为true就不用轮询其他集群
        if ("true".equals(all)){
            //构造请求参数
            Map<String,String> reqMap = new HashMap<String, String>();
            reqMap.put("all","false");
            //轮询集群
            for (String url : urlList){
                //进行5次尝试
                for (int i = 5; i > 0; i--){
                    String response = sendPost(url,reqMap);
                    if (response.contains(Constant.STRING_SUCCESS_MSG)){
                        res = Constant.STRING_SUCCESS_MSG;
                        break;
                    } else {
                        res = Constant.STRING_FAILD_MSG;
                    }
                }
            }
        }
        return res;
    }

    /**
     * 启动时加载配置文件
     */
    @PostConstruct
    private void getConfig(){
        try {
            //获取本机IP
            String local = InetAddress.getLocalHost().getHostAddress();

            urlList = new ArrayList<>();
            String[] split = quorum.split(",");
            for (String str : split){
                //排除本机IP
                if (str.contains(local)){
                    continue;
                }
                //添加队列
                urlList.add("http://".concat(str).concat("/iflytek/api/cache/refresh"));
            }
        } catch (UnknownHostException e){
            log.error("获取本机IP失败");
        }
    }

    /**
     * 发送HTTP POST请求
     * @param address
     * @return
     */
    private String sendPost(String address,Map<String,String> urlParam) {
        try {
            StringBuffer buffer = new StringBuffer();
            final StringBuilder parambuider = new StringBuilder();
            if (urlParam != null && urlParam.size() > 0){
                for (Map.Entry<String,String> entry : urlParam.entrySet()){
                    parambuider.append(entry.getKey()).append("=");
                    parambuider.append(URLEncoder.encode(entry.getValue(),"utf8")).append("&");
                }
            }
            URL url = new URL(address + (parambuider.length() > 0 ? "?" + parambuider.substring(0,parambuider.length() - 1) : ""));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(10000);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                @Cleanup
                BufferedReader bfr = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String temp;
                while ((temp = bfr.readLine()) != null) {
                    buffer.append(temp);
                }
                log.info("请求发送成功：url=" + address);
                return buffer.toString();
            } else {
                log.info("请求发送失败：url=" + address);
                return "";
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return "";
        }
    }
}
