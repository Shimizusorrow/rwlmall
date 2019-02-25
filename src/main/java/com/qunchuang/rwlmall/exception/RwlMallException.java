package com.qunchuang.rwlmall.exception;

import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import lombok.Getter;

/**
 * @author Curtain
 * @date 2018/3/12 16:02
 */

@Getter
public class RwlMallException extends RuntimeException{

    private ResultExceptionEnum resultExceptionEnum;

    public RwlMallException(String msg){
        super(msg);
    }

    public RwlMallException(ResultExceptionEnum exceptionEnum){
        super(exceptionEnum.getMessage());
        this.resultExceptionEnum = exceptionEnum;
    }

    public RwlMallException(ResultExceptionEnum exceptionEnum, String msg){
        super(exceptionEnum.getMessage()+", "+msg);
        this.resultExceptionEnum = exceptionEnum;
    }

}
