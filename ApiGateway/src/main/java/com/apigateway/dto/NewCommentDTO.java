package com.apigateway.dto;

public class NewCommentDTO {

    private String userId;
    private String text;

    public NewCommentDTO() {
    }


    public NewCommentDTO(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
