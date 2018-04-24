package com.iflytek.service.impl;

import com.iflytek.dao.HBaseDao;
import com.iflytek.entity.req.RecommendCommonParams;
import com.iflytek.entity.res.ResponseContent;
import com.iflytek.entity.res.ResponseItem;
import com.iflytek.exception.exceptionhandle.SecurityVerificationException;
import com.iflytek.service.RecService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class RecServiceImpl implements RecService {

    @Autowired
    private HBaseDao hBaseDao;

    @Override
    public ResponseContent getAllRec(RecommendCommonParams reqParams) {
        ResponseContent content = new ResponseContent();
        List<ResponseItem> results = new ArrayList<>();
        ResponseItem responseItem = new ResponseItem();
        responseItem.setUid("");
        responseItem.setResult_id("11111~222222~33333");
        results.add(responseItem);
        content.setResults(results);
        return content;
    }

    @Override
    public ResponseContent getMovieRec(RecommendCommonParams reqParams) {
        return null;
    }

    @Override
    public ResponseContent getDbRec(RecommendCommonParams reqParams) throws IOException{
        //点播结果处理业务结果
        List<ResponseItem> results = new ArrayList<>();
        String uids = reqParams.getContent().getUids();
        log.debug("uuids:{}",uids);
        if(StringUtils.isNotEmpty(uids)){
            if(uids.contains(",")){
                for(String uuid:uids.split(",")){
                    String result = this.handResult(hBaseDao.getDbResult(uuid,"rc"),100);
                    results.add(new ResponseItem(uuid,result));
                }
            }else{
                String result = this.handResult(hBaseDao.getKdResult(uids,"rc"),100);
                results.add(new ResponseItem(uids,result));
            }
        }else {
            throw new SecurityVerificationException("the params of uids is required");
        }
        return new ResponseContent(results);
    }

    @Override
    public ResponseContent getKdRec(RecommendCommonParams reqParams) throws IOException {
        //看点结果查询业务处理
        List<ResponseItem> results = new ArrayList<>();
        String uids = reqParams.getContent().getUids();
        log.debug("uuids:{}",uids);
        if(StringUtils.isNotEmpty(uids)){
            if(uids.contains(",")){
                for(String uuid:uids.split(",")){
                    String result = this.handResult(hBaseDao.getKdResult(uuid,"rc"),100);
                    results.add(new ResponseItem(uuid,result));
                }
            }else{
                String result = this.handResult(hBaseDao.getKdResult(uids,"rc"),100);
                results.add(new ResponseItem(uids,result));
            }
        }else {
            throw new SecurityVerificationException("the params of uids is required");
        }
        return new ResponseContent(results);
    }

    @Override
    public ResponseContent getSeriesRec(RecommendCommonParams reqParams) {
        return null;
    }

    @Override
    public ResponseContent getOtherRec(RecommendCommonParams reqParams) {
        return null;
    }

    /**
     *  处理公共的结果
     * @param result 原始结果
     * @param num 需要的条数
     * @return
     */
    private String handResult(String result,int num){
        if(StringUtils.isNotEmpty(result)){
            List<String> results = Arrays.asList(result.split("~"));
            return results.subList(0,num).stream().map(Object::toString)
                    .collect(Collectors.joining("~"));
        }else {
            log.debug("can't find result from HBase");
            return "";
        }

    }
}
