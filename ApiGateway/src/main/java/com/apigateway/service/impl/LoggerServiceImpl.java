package com.apigateway.service.impl;

import com.apigateway.service.LoggerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerServiceImpl implements LoggerService {

    private final Logger logger;

    public LoggerServiceImpl(Class<?> parentClass) {
        this.logger = LogManager.getLogger(parentClass);
    }


    @Override
    public void grpcConnectionFailed(String method, String path) {
        logger.error("Grpc connection failed for request {}: {}", method, path);
    }

    @Override
    public void unauthorizedAccess(String method, String path, String ip) {
        logger.warn("Unauthorized access to {}: {}. From: {}", method, path, ip);
    }
}
