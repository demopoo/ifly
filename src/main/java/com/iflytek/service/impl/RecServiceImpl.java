package com.iflytek.service.impl;

import com.iflytek.entity.req.RecommendCommonParams;
import com.iflytek.entity.res.ResponseContent;
import com.iflytek.entity.res.ResponseItem;
import com.iflytek.service.RecService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class RecServiceImpl implements RecService {

    @Override
    public ResponseContent getAllRec(RecommendCommonParams reqParams) {
        ResponseContent content = new ResponseContent();
        List<ResponseItem> results = new ArrayList<>();
        ResponseItem responseItem = new ResponseItem();
        responseItem.setUid(111);
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
    public ResponseContent getFocusRec(RecommendCommonParams reqParams) {
        return null;
    }

    @Override
    public ResponseContent getSeriesRec(RecommendCommonParams reqParams) {
        return null;
    }

    @Override
    public ResponseContent getOtherRec(RecommendCommonParams reqParams) {
        return null;
    }
}
