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
    public void changePasswordRecoverFailed(String message, String ip) {
        logger.warn("Password recover failed: {}. From: {}", message, ip);
    }

    @Override
    public void passwordRecovered(String ip) {
        logger.info("Password recovered successfully. From {}", ip);
    }

    @Override
    public void passwordlessLoginFailed(String message, String ip) {
        logger.warn("Passwordless login failed: {}. From: {}", message, ip);
    }

    @Override
    public void passwordlessLoginSuccess(String ip) {
        logger.info("Passwordless login successful. From: {}", ip);
    }

    @Override
    public void apiTokenGenerated(String userId) {
        logger.info("API token successfully generated. User id: {}", userId);
    }

    @Override
    public void recommendationsSucceed(String userId) {
        logger.info("Recommendations successfully gotten. User id: {}", userId);
    }

    @Override
    public void recommendationsFailed(String userId) {
        logger.warn("Recommendations failed. User id: {}", userId);
    }
}
