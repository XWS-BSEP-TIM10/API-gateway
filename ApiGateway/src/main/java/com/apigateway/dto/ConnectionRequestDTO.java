package com.apigateway.dto;

import javax.validation.constraints.NotBlank;

public class ConnectionRequestDTO {

    @NotBlank
    private String connectingId;


    public String getConnectingId() {
        return connectingId;
    }
}
