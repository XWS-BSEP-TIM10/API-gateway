package com.apigateway.dto;

public class APITokenResponseDTO {
    private String token;

    public APITokenResponseDTO(String token) {
        this.token = token;
    }

    public APITokenResponseDTO() {
    }

    public String getToken() {
        return token;
    }
}
