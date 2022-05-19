package com.apigateway.dto;

public class PasswordDto {
    private String newPassword;
    private String repeatedNewPassword;

    public PasswordDto() {
    }

    public PasswordDto(String newPassword, String repeatedNewPassword) {
        this.newPassword = newPassword;
        this.repeatedNewPassword = repeatedNewPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getRepeatedNewPassword() {
        return repeatedNewPassword;
    }

}
