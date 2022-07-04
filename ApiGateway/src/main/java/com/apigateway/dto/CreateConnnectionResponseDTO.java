package com.apigateway.dto;

public class CreateConnnectionResponseDTO {
    private final String connectionStatus;

    public CreateConnnectionResponseDTO(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }
}
