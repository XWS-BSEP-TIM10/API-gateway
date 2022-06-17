package com.apigateway.service;

public interface LoggerService {

    void grpcConnectionFailed(String method, String path);

    void unauthorizedAccess(String method, String path, String ip);

    void changePasswordRecoverFailed(String message, String email, String ip);

    void passwordRecovered(String email, String ip);

    void passwordlessLoginFailed(String message, String email, String ip);

    void passwordlessLoginSuccess(String email, String ip);

    void apiTokenGenerated(String userId);
}
