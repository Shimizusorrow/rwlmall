package com.qunchuang.rwlmall.handler;

import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.utils.ResultVOUtil;
import com.qunchuang.rwlmall.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Curtain
 * @date 2018/3/9 15:07
 */

@ControllerAdvice
@Slf4j
public class RwlMallExceptionHandler {

    @ResponseBody
    @ExceptionHandler
    //@ResponseStatus(HttpStatus.FORBIDDEN)   设置返回码
    public ResultVO handler(Exception e) throws Exception {
        if (e instanceof RwlMallException) {
            log.error("Controller : RwlMallException = " + e.getMessage());
            log.error("ResultExceptionEnum = " + ((RwlMallException) e).getResultExceptionEnum().toString());
            return ResultVOUtil.error(((RwlMallException) e).getResultExceptionEnum());
        } else if (e instanceof BadCredentialsException) {
            log.error("Controller : BadCredentialsException = " + e.getMessage());
            throw e;
        } else if (e instanceof AccessDeniedException) {
            log.error("Controller : AccessDeniedException = " + e.getMessage());
            throw e;
        } else if (e instanceof MissingServletRequestParameterException) {
            log.error("Controller : MissingServletRequestParameterException = " + e.getMessage());
            return ResultVOUtil.error(ResultExceptionEnum.REQUEST_FAIL);
        } else {
            log.error("异常信息:" + e.getMessage());
            log.error("异常堆栈信息:" + collectExceptionStackMsg(e));
            return ResultVOUtil.error("系统内部错误，请联系管理员或者添加反馈！");
        }
    }

    /*收集异常堆栈信息*/
    public static String collectExceptionStackMsg(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        String str = sw.toString();
        return str;
    }
}
