package com.gq.crm.proxy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @date 2023/6/19
 */


@Component
@Aspect
public class PermissionProxy {

    @Around(value = "")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        Object result = null;
        result = pjp.proceed();
        return  result;
    }
}
