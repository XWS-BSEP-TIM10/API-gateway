package com.apigateway.dto;

import javax.validation.constraints.NotBlank;

public class ConnectionRequestDTO {

    @NotBlank
    private String initiatorId;
    @NotBlank
    private String receiverId;


    public String getInitiatorId() {
        return initiatorId;
    }

    public String getReceiverId() {
        return receiverId;
    }
}
