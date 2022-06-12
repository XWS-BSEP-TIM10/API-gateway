package com.apigateway.service;

public interface LoggerService {


    void grpcConnectionFailed(String method, String path);

    void unauthorizedAccess(String method, String path, String ip);
}
