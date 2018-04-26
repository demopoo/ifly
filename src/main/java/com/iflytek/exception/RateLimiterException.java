package com.iflytek.exception;


/**
 * 限流异常处理
 * @author yu
 */
public class RateLimiterException extends RuntimeException {

    public RateLimiterException(String message) {
        super(message);
    }
}
