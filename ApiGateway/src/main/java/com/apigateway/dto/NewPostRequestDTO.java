package com.apigateway.dto;

import javax.validation.constraints.NotBlank;

public class NewPostRequestDTO {
    @NotBlank
    private String ownerId;
    private String text;

    public NewPostRequestDTO() {
    }

    public NewPostRequestDTO(String ownerId, String text) {
        this.ownerId = ownerId;
        this.text = text;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getText() {
        return text;
    }
}
