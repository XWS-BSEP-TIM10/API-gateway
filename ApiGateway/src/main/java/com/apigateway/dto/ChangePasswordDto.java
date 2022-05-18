package com.apigateway.dto;

public class ChangePasswordDto {
    private String userId;
    private String oldPassword;
    private String newPassword;
    private String repeatedNewPassword;

    public ChangePasswordDto() {
    }

    public ChangePasswordDto(String userId, String oldPassword, String newPassword, String repeatedNewPassword) {
        this.userId = userId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.repeatedNewPassword = repeatedNewPassword;
    }

    public String getUserId() {
        return userId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getRepeatedNewPassword() {
        return repeatedNewPassword;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setRepeatedNewPassword(String repeatedNewPassword) {
        this.repeatedNewPassword = repeatedNewPassword;
    }
}
