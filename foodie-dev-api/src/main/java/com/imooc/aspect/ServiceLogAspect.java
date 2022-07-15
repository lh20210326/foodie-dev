package com.imooc.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
@Aspect
@Component
public class ServiceLogAspect {
    public static final Logger log= LoggerFactory.getLogger(ServiceLogAspect.class);
    @Around("execution(* com.imooc.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info("===开始执行{}.{}===",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());
        //记录开始时间
        long begin=System.currentTimeMillis();
        Object result = joinPoint.proceed();
        //记录结束时间
        long end=System.currentTimeMillis();
        long takeTime=end-begin;
        if(takeTime>3000){
            log.error("===执行结束，耗时：{}===",takeTime);
        }else if(takeTime>2000){
            log.error("===执行结束，耗时：{}===",takeTime);
        }else{
            log.info("===执行结束，耗时：{}===",takeTime);

        }
        return result;
    }
}
