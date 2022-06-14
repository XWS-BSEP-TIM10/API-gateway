package com.apigateway.dto;

import javax.validation.constraints.NotBlank;

public class LoginDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String code;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCode() {
        return code;
    }
}
