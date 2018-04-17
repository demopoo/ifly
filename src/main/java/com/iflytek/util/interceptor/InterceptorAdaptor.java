package com.iflytek.util.interceptor;import com.fasterxml.jackson.dataformat.xml.XmlMapper;import com.iflytek.entity.req.RecommendCommenParams;import com.iflytek.entity.req.RequestContent;import com.iflytek.util.VerificationSignature;import org.apache.commons.io.IOUtils;import org.apache.commons.io.input.XmlStreamReader;import org.springframework.web.servlet.HandlerInterceptor;import org.springframework.web.servlet.ModelAndView;import javax.servlet.http.HttpServletRequest;import javax.servlet.http.HttpServletResponse;import java.io.InputStream;import java.io.ObjectInputStream;import java.nio.charset.StandardCharsets;/** * @author: demopoo * @Date: Created in 下午6:16 2018/4/16 * @Des: 拦截器，用于验证签名 ，以及后续请求耗时统计 * @Modifyed By: */public class InterceptorAdaptor implements HandlerInterceptor {    private ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();    /**     * 验证     * @param httpServletRequest     * @param httpServletResponse     * @param o     * @return     * @throws Exception     */    @Override    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {//        Long startTime = System.currentTimeMillis();//        Map<String,String[]> map = httpServletRequest.getParameterMap();//        Set<Map.Entry<String,String[]>> entrySet = map.entrySet();//        StringBuffer stringBuffer = new StringBuffer();//        stringBuffer.append("secret");//        for (Map.Entry<String,String[]> entry : entrySet ){//            if (!entry.getKey().equals("sign")){//                stringBuffer.append(entry.getKey()+entry.getValue()[0]);//            }//        }//        String signParam = null;//        if(map.containsKey("sign")){//            signParam = map.get("sign")[0];//        }//        stringBuffer.append("secret");//        String autographResult = VerificationSignature.encrypt(stringBuffer.toString(),"demopoodemopoooo");//        threadLocal.set(startTime);//        httpServletResponse.sendError(203);//        httpServletResponse.getWriter().write("sorry , 验证签名失败");        //以下是调用申请接入接口的安全认证        //以下是调用推荐接口进行安全认证,咱不开启（已完成）//        String requestBody= IOUtils.toString(httpServletRequest.getInputStream(), StandardCharsets.UTF_8);//        XmlMapper xmlMapper = new XmlMapper();//        RecommendCommenParams recommendCommenParams = xmlMapper.readValue(requestBody,RecommendCommenParams.class);//        RequestContent content = recommendCommenParams.getContent();//        VerificationSignature.validArgs(content);        return true;    }    /**     * 基础处理完成，还未发送给前端     * @param httpServletRequest     * @param httpServletResponse     * @param o     * @param modelAndView     * @throws Exception     */    @Override    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {    }    /**     * 整个请求执行完毕回调     * @param httpServletRequest     * @param httpServletResponse     * @param o     * @param e     * @throws Exception     */    @Override    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {    }}