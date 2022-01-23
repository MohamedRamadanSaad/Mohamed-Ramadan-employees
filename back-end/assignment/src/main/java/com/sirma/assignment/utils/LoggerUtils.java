package com.sirma.assignment.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class LoggerUtils {
    private Logger logger = null;

    public void logErrorMessage(final Class cls, final String... msg) {
        setLoggerClass(cls);
        logger.error(Arrays.toString(msg));
    }

    public void logInfoMessage(final Class cls, final String... msg) {
        setLoggerClass(cls);
        logger.info(Arrays.toString(msg));
    }

    public void logErrorAndThrowException(final Class cls, final String... msg) {
        setLoggerClass(cls);
        logger.error(Arrays.toString(msg));
        throw new RuntimeException(Arrays.toString(msg));
    }

    public void logErrorAndThrowExceptionPrintStackTrace(final Class cls, final Exception e, final String... msg) {
        setLoggerClass(cls);
        logger.error(Arrays.toString(msg));
        e.printStackTrace();
        throw new RuntimeException(Arrays.toString(msg));
    }

    private void setLoggerClass(final Class cls) {
        logger = LoggerFactory.getLogger(cls);
    }

    public Logger getLogger(final Class cls) {
        return LoggerFactory.getLogger(cls);
    }
}
