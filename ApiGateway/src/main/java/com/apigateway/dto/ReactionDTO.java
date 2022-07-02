package com.apigateway.dto;

public class ReactionDTO {

    private String userId;
    private Boolean like;

    public ReactionDTO() {
        /*dto*/
    }

    public Boolean isLike() {
        return like;
    }

    public String getUserId() {
        return userId;
    }
}
