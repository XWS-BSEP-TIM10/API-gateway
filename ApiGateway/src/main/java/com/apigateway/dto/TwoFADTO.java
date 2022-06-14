package com.apigateway.dto;

public class TwoFADTO {
    private boolean enable2FA;

    public String userId;

    public String getUserId() {
        return userId;
    }

    public boolean isEnable2FA() {
        return enable2FA;
    }
}
