package com.iflytek.component;

import com.iflytek.entity.MockResult;
import com.iflytek.entity.req.RecommendCommonParams;
import com.iflytek.entity.res.RecommendCommonResult;
import com.iflytek.exception.exceptionhandle.SecurityVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import java.io.IOException;

/**
 * 参数校验和异常处理AOP
 * @author jjliu15@iflytek.com
 * @date 2018/1/16
 */
@Slf4j
@Aspect
@Component
public class ExceptionInterceptor {

    @Autowired
    private ParamValid paramValid;

    /**
     * 环绕通知处理参数校验和Controller异常
     * @param point
     * @param params
     * @return
     */
    @Around(value = "execution(* com.iflytek.rest.RecommendController.*(..)) && args(params)")
    RecommendCommonResult handleControllerMethod(ProceedingJoinPoint point, RecommendCommonParams params){
        log.debug("request param from ExceptionInterceptor:" + params.toString());
        RecommendCommonResult result;
        try {
            //执行参数校验
            paramValid.validArgs(params.getContent());
            //执行切点方法
            result = (RecommendCommonResult) point.proceed(point.getArgs());
        } catch (ConstraintViolationException e){
            log.error("参数校验异常",e);
            result = MockResult.returnErrorResult(params,"ILLEGAL ARGS","9002");
        } catch (SecurityVerificationException e){
            result = MockResult.returnErrorResult(params,"SECURITY CHECK FAILED","9004");
        } catch (IOException e){
            log.error("Hbase异常",e);
            result = MockResult.returnErrorResult(params,"SERVER ERROR","9003");
        } catch (Throwable e){
            log.error("未知错误",e);
            result = MockResult.returnErrorResult(params,"UNKNOWN ERROR","9999");
        }
        return result;
    }

}
