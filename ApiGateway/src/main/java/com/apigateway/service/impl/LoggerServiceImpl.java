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

    @Override
    public void changePasswordRecoverFailed(String message, String email, String ip) {
        logger.warn("Password recover failed: {}. Email: {}. From: {}", message, email, ip);
    }

    @Override
    public void passwordRecovered(String email, String ip) {
        logger.info("Password recovered successfully. Email: {}. From {}", email, ip);
    }

    @Override
    public void passwordlessLoginFailed(String message, String email, String ip) {
        logger.warn("Passwordless login failed: {}. Email: {}. From: {}", message, email, ip);
    }

    @Override
    public void passwordlessLoginSuccess(String email, String ip) {
        logger.info("Passwordless login successful. Email: {}. From: {}", email, ip);
    }

    @Override
    public void apiTokenGenerated(String userId) {
        logger.info("API token successfully generated. User id: {}", userId);
    }
}
