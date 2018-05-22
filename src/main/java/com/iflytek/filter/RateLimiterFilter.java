package com.iflytek.filter;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.iflytek.common.util.concurrent.RateLimiter;
import com.iflytek.entity.MockResult;
import com.iflytek.entity.req.RecommendCommonParams;
import com.iflytek.entity.res.RecommendCommonResult;
import com.iflytek.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 限流过滤器
 * @author yu
 */
@Slf4j
public class RateLimiterFilter implements Filter {

    /**
     * tps
     */
    public static final String TPS = "tps";
    /**
     * drop
     */
    public static final String IS_DROP = "isDrop";


    /**
     * 限流器
     */
    private RateLimiter limiter = null;

    /**
     * 超流是否需要丢弃
     */
    private boolean isDrop;

    /**
     * tps
     */
    private Double tps;

    @Override
    public void init(FilterConfig filterConfig) {
        //获取tps限流量,如果配置错误将配设置成100
        tps = NumberUtils.toDouble(filterConfig.getInitParameter(TPS), 100);
        log.debug("current tps set is:{}",tps);
        isDrop = Boolean.valueOf(filterConfig.getInitParameter(IS_DROP));
        limiter = RateLimiter.create(tps); //100 request per second
    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (isDrop) {
            if (limiter.tryAcquire()) {
                log.debug("get access:");
                chain.doFilter(request, response);
            } else {
                HttpServletRequest req = (HttpServletRequest) request;
                HttpServletResponse res = (HttpServletResponse) response;


                log.error("TOO MANY REQUESTS,current tps set is {}",tps);

                String xml = WebUtil.getStream(req);
                XmlMapper mapper = new XmlMapper();

                RecommendCommonParams params = mapper.readValue(xml, RecommendCommonParams.class);
                RecommendCommonResult result = MockResult.returnErrorResult(params, "9005", "TOO MANY REQUESTS");

                res.getWriter().write(mapper.writeValueAsString(result));
            }
        } else {
            log.debug("access wait");
            limiter.acquire();
            chain.doFilter(request, response);
        }

    }
}
