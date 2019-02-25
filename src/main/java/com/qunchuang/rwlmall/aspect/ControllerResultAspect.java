package com.qunchuang.rwlmall.aspect;

import com.qunchuang.rwlmall.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@RestController
@Slf4j
public class ControllerResultAspect {

    //切点  所有controller包下的的公有方法
    @Pointcut("execution(public * com.qunchuang.rwlmall.controller.*.*(..))")
    public void returnResult(){}

    //为所有的Controller类的返回值   做一个统一的处理
    @Around("returnResult()")
    public Object doAfter(ProceedingJoinPoint pjp) throws Throwable {
        Object retVal = pjp.proceed();
        return ResultVOUtil.success(retVal);
    }

    //测试时使用  方便对接API
    @Before("returnResult()")
    public void doBefore(JoinPoint joinPoint){
        //获取请求的内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //url
        log.info("url = " + request.getRequestURI());

/*
        //method
        log.info("method={}", request.getMethod());

        //ip
        log.info("ip={}", request.getRemoteAddr());

        //类方法
        log.info("class_method={}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
*/
        //参数
//        log.info("args={}", joinPoint.getArgs().length);

    }
}
