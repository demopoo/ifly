package com.iflytek.rest;

import com.iflytek.entity.Constant;
import com.iflytek.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存接口
 * @author jjliu15@iflytek.com
 * @date 2018/1/22
 */
@RestController
@RequestMapping(value = "/iflytek/api/cache")
public class CacheController {

    @Autowired
    private CacheService cacheService;

    @PostMapping(value = "/refresh", consumes = {"text/json","application/json"})
    public Map<String,Object> refresh(@RequestParam(value = "all", required = false) String all){
        String res = cacheService.clearCache(all);
        Map<String,Object> responseMap = new HashMap<String,Object>();
        responseMap.put("code",res.equals(Constant.STRING_SUCCESS_MSG) ? Constant.STRING_SUCCESS_CODE : Constant.STRING_FAILD_CODE);
        responseMap.put("msg",res);
        return responseMap;
    }

}
