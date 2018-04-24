package com.iflytek.service;

import com.iflytek.entity.req.RecommendCommonParams;
import com.iflytek.entity.res.ResponseContent;

import java.io.IOException;

/**
 * 获取所有结果的service
 * @author yusun4@iflytek.com
 */
public interface RecService {

    /**
     * 获取全量推荐
     * @param reqParams
     * @return
     * @throws IOException
     */
    ResponseContent getAllRec(RecommendCommonParams reqParams) throws IOException;

    /**
     * 获取电影推荐
     * @param reqParams
     * @return
     * @throws IOException
     */
    ResponseContent getMovieRec(RecommendCommonParams reqParams) throws IOException;

    /**
     * 获取电视剧推荐
     * @param reqParams
     * @return
     * @throws IOException
     */
    ResponseContent getSeriesRec(RecommendCommonParams reqParams) throws IOException;

    /**
     * 获取其他推荐
     * @param reqParams
     * @return
     * @throws IOException
     */
    ResponseContent getOtherRec(RecommendCommonParams reqParams) throws IOException;

    /**
     * 获取看点推荐
     * @param reqParams
     * @return
     * @throws IOException
     */
    ResponseContent getFocusRec(RecommendCommonParams reqParams) throws IOException;

}
