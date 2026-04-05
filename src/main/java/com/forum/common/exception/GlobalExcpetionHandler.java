package com.forum.common.exception;

import com.forum.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExcpetionHandler {
    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result hnadleBusinessException(BusinessException e){
        return Result.error(e.getCode(),e.getMessage());
    }
}
