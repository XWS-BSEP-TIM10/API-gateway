package com.apigateway.dto;

public class CommentDTO {
    private String text;
    private String ownerId;

    public CommentDTO() {
    }

    public CommentDTO(String text, String ownerId) {
        this.text = text;
        this.ownerId = ownerId;
    }

    public String getText() {
        return text;
    }

    public String getOwnerId() {
        return ownerId;
    }
}
