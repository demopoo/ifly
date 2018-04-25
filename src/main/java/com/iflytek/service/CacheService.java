package com.iflytek.service;

/**
 * 缓存服务
 * @author jjliu15@iflytek.com
 * @date 2018/1/22
 */
public interface CacheService {

    /**
     * 清空缓存
     * @param all
     * @return
     */
    String clearCache(String all);

}
