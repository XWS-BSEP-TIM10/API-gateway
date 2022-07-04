package com.apigateway.service;

public interface LoggerService {

    void grpcConnectionFailed(String method, String path);

    void unauthorizedAccess(String method, String path, String ip);

    void changePasswordRecoverFailed(String message, String ip);

    void passwordRecovered(String ip);

    void passwordlessLoginFailed(String message, String ip);

    void passwordlessLoginSuccess(String ip);

    void apiTokenGenerated(String userId);

    void recommendationsSucceed(String userId);

    void recommendationsFailed(String userId);
}
