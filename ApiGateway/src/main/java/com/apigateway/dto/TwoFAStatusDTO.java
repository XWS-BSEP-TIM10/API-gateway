package com.apigateway.dto;

public class TwoFAStatusDTO {

    private boolean enabled2FA;

    public TwoFAStatusDTO() {
    }

    public TwoFAStatusDTO(boolean enabled2FA) {
        this.enabled2FA = enabled2FA;
    }

    public boolean isEnabled2FA() {
        return enabled2FA;
    }
}
