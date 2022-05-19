package com.apigateway.dto;

public class ConnectionStatusDto {

    private String connectionStatus;

    public ConnectionStatusDto() {
    }

    public ConnectionStatusDto(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }
}
