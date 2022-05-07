package com.apigateway.dto;

public class NewPostResponseDTO {
    private String id;

    public NewPostResponseDTO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
