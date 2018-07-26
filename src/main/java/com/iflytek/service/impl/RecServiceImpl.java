package com.iflytek.service.impl;

import com.iflytek.dao.HBaseDao;
import com.iflytek.entity.req.RecommendCommonParams;
import com.iflytek.entity.res.ResponseContent;
import com.iflytek.entity.res.ResponseItem;
import com.iflytek.exception.SecurityVerificationException;
import com.iflytek.service.RecService;
import com.iflytek.util.CollectionUtil;
import com.iflytek.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class RecServiceImpl implements RecService {


    @Autowired
    private HBaseDao hBaseDao;

    @Value("${application.limit}")
    private int num;

    @Override
    public ResponseContent getAllRec(RecommendCommonParams reqParams) throws IOException {
        long start = System.currentTimeMillis();
        List<ResponseItem> results = new ArrayList<>();
        String uids = reqParams.getContent().getUids();
        log.debug("uuids:{}", uids);
        int subListNums = num >> 1;
        if (StringUtils.isNotEmpty(uids)) {
            if (uids.contains(",")) {
                for (String uuid : uids.split(",")) {
                    String strDbResult = hBaseDao.getDbResult(uuid, "rec");
                    //获取点播结果
                    String strKdResult = hBaseDao.getKdResult(uuid, "rec");
                    List<String> dbResult = CollectionUtil.subList(CollectionUtil.asList(StringUtil.split(strDbResult, "~")), 0, subListNums);

                    //获取看点结果
                    List<String> kdResult = CollectionUtil.subList(CollectionUtil.asList(StringUtil.split(strKdResult, "~")), 0, this.num - subListNums);

                    //点播和看点结果
                    List<String> mergeResult = CollectionUtil.mergeAndSwap(dbResult, kdResult);

                    results.add(new ResponseItem(uuid, removeRepeatAndCastToStr(mergeResult)));

                }

            } else {
                String strDbResult = hBaseDao.getDbResult(uids, "rec");
                //获取点播结果
                List<String> dbResult = CollectionUtil.subList(CollectionUtil.asList(StringUtil.split(strDbResult, "~")), 0, subListNums);
                //获取看点结果
                String strKdResult = hBaseDao.getKdResult(uids, "rec");
                List<String> kdResult = CollectionUtil.subList(CollectionUtil.asList(StringUtil.split(strKdResult, "~")), 0, this.num - subListNums);
                //点播和看点结果
                List<String> mergeResult = CollectionUtil.mergeAndSwap(dbResult, kdResult);
                //将合并后的结果处理并存储
                results.add(new ResponseItem(uids, removeRepeatAndCastToStr(mergeResult)));

            }
        } else {
            log.error("the params of uids is required");
            throw new SecurityVerificationException("the params of uids is required");
        }
        log.debug("getAllRec costTime:{}", (System.currentTimeMillis() - start));
        return new ResponseContent(results);
    }

    @Override
    public ResponseContent getMovieRec(RecommendCommonParams reqParams) {
        return null;
    }

    @Override
    public ResponseContent getDbRec(RecommendCommonParams reqParams) throws IOException {
        //点播结果处理业务结果
        List<ResponseItem> results = new ArrayList<>();
        String uids = reqParams.getContent().getUids();
        log.debug("uuids:{}", uids);
        if (StringUtils.isNotEmpty(uids)) {
            if (uids.contains(",")) {
                for (String uuid : uids.split(",")) {
                    String result = this.handleResult(hBaseDao.getDbResult(uuid, "rec"), this.num);
                    results.add(new ResponseItem(uuid, result));
                }
            } else {
                String result = this.handleResult(hBaseDao.getDbResult(uids, "rec"), this.num);
                results.add(new ResponseItem(uids, result));
            }
        } else {
            log.error("the params of uids is required");
            throw new SecurityVerificationException("the params of uids is required");
        }
        return new ResponseContent(results);
    }

    @Override
    public ResponseContent getKdRec(RecommendCommonParams reqParams) throws IOException {
        //看点结果查询业务处理
        List<ResponseItem> results = new ArrayList<>();
        String uids = reqParams.getContent().getUids();
        log.debug("uuids:{}", uids);
        if (StringUtils.isNotEmpty(uids)) {
            if (uids.contains(",")) {
                for (String uuid : uids.split(",")) {
                    String result = this.handleResult(hBaseDao.getKdResult(uuid, "rec"), this.num);
                    results.add(new ResponseItem(uuid, result));
                }
            } else {
                String result = this.handleResult(hBaseDao.getKdResult(uids, "rec"), this.num);
                results.add(new ResponseItem(uids, result));
            }
        } else {
            log.error("the params of uids is required");
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
     * 处理公共的结果
     *
     * @param result 原始结果
     * @param num    需要的条数
     * @return
     */
    private String handleResult(String result, int num) {
        if (StringUtils.isNotEmpty(result)) {
            List<String> results = Arrays.asList(result.split("~"));
            return results.subList(0, num).stream().map(Object::toString)
                    .collect(Collectors.joining("~"));
        } else {
            log.debug("can't find result from HBase");
            return "";
        }

    }

    /**
     * 处理重复结果并且将其转成string
     *
     * @param results
     * @return
     */
    private String removeRepeatAndCastToStr(List<String> results) {
        Set<String> set = new TreeSet<>();
        set.addAll(results);
        //使用jdk1.8 lambda
        return set.stream().map(i -> i.toString()).collect(Collectors.joining("~"));
    }

    /**
     * 将list转化成string
     *
     * @param results
     * @return
     */
    private String handleListResultToStr(List<String> results) {
        return results.stream().map(i -> i.toString())
                .collect(Collectors.joining("~"));
    }
}
