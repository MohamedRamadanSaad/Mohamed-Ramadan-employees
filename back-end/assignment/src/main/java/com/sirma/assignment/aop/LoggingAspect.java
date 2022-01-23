package com.sirma.assignment.aop;

import com.sirma.assignment.utils.LoggerUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;


@Aspect
@Configuration
public class LoggingAspect {
    private final LoggerUtils loggerUtils;

    public LoggingAspect(final LoggerUtils loggerUtils) {
        this.loggerUtils = loggerUtils;
    }

    @Before("execution(* com.sirma.assignment.controllers.*.*(..))")
    public void logBeforeSecurityMethods(final JoinPoint joinPoint) {
        loggerUtils.logInfoMessage(joinPoint.getTarget().getClass(), "log Before Controller Methods: Access method: ",
                joinPoint.getTarget().getClass() + ".",
                joinPoint.getSignature().getName());
    }

    @Before("execution(* com.sirma.assignment.services.*.*(..))")
    public void logBeforeServiceMethods(final JoinPoint joinPoint) {
        loggerUtils.logInfoMessage(joinPoint.getTarget().getClass(), "log Before Services Methods: Access method: ",
                joinPoint.getTarget().getClass() + ".",
                joinPoint.getSignature().getName());
    }

}