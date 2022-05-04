package com.apigateway.dto;

public class NewCommentDTO {

    private String text;

    public NewCommentDTO() {
    }

    public NewCommentDTO(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
