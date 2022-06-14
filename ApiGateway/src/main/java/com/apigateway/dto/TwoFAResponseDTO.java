package com.apigateway.dto;

public class TwoFAResponseDTO {
    private String secret;

    public TwoFAResponseDTO(String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }
}
