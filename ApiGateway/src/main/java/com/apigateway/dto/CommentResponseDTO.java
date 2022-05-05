package com.apigateway.dto;

public class CommentResponseDTO {

    private String text;

    public CommentResponseDTO() {
    }

    public CommentResponseDTO(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
