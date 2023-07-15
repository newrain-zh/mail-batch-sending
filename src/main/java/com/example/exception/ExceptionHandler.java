package com.example.exception;

import com.example.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 自定义异常处理
 *
 * @author alex
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandler {


    /**
     * 处理BusinessException异常
     *
     * @param e
     * @return
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public Result<?> business(BusinessException e) {
        return Result.error(e.getCode(), e.getMessage());
    }
}