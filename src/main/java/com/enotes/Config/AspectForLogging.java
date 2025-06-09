package com.enotes.Config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AspectForLogging {


    @Around("execution(* com.enotes.Controller..*(..))")
    public Object loggerForController(ProceedingJoinPoint joinPoint) throws Throwable {
        return getObject(joinPoint);
    }
    @Around("execution(* com.enotes.Service..*(..))")
    public Object loggerForService(ProceedingJoinPoint joinPoint) throws Throwable {
        return getObject(joinPoint);
    }

    private Object getObject(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        log.info("Calling :: {} :: {}()",className,methodName);
        Object proceed = joinPoint.proceed();
        long duration = System.currentTimeMillis()-startTime;
        log.info("End :: {} :: {}()  duration:{}ms",className,methodName,duration);
        return proceed;
    }
}
