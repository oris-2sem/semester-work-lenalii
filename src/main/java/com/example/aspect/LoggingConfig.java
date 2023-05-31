package com.example.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Aspect
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "logger", value = "enabled", havingValue = "true")
public class LoggingConfig {

    private static final String AROUND_METHOD_MESSAGE_PATTERN = "method with signature: {}, returned: {}";

    private static final String EXCEPTION_PATTERN = "method with signature {} failed with exception message: {}";


    @Pointcut(value = "execution(* com.example.service.*.*(..))")
    public void allMethodPointcut(){}

    @Around(value = "allMethodPointcut()")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Object[] args = proceedingJoinPoint.getArgs();
        try {
            Object result = proceedingJoinPoint.proceed(args);
            log.info(AROUND_METHOD_MESSAGE_PATTERN, proceedingJoinPoint.getSignature(), result);
            return result;
        }catch (Throwable e){
            log.info(EXCEPTION_PATTERN, proceedingJoinPoint.getSignature(), e.getMessage());
            throw e;
        }
    }
}
