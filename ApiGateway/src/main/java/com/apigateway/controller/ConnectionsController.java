package com.apigateway.controller;

import com.apigateway.dto.ConnectionRequestDTO;
import com.apigateway.service.ConnectionsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proto.ConnectionResponseProto;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1")
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    public ConnectionsController(ConnectionsService connectionsService) {
        this.connectionsService = connectionsService;
    }

    @PostMapping(value = "users/{id}/connections")
    public ResponseEntity<HttpStatus> connect(@RequestBody @Valid ConnectionRequestDTO newConnectionRequestDTO, @PathVariable String id) {
        ConnectionResponseProto connectionResponseProto = connectionsService.createConnection(id, newConnectionRequestDTO.getConnectingId());
        if (connectionResponseProto.getStatus().equals("Status 400"))
            return ResponseEntity.badRequest().build();
        else if (connectionResponseProto.getStatus().equals("Status 200"))
            return ResponseEntity.ok().build();
        return ResponseEntity.internalServerError().build();
    }

    @PutMapping("users/{id}/connections/approve")
    public ResponseEntity<HttpStatus> approveConnectionRequest(@RequestBody @Valid ConnectionRequestDTO connectionRequestDto, @PathVariable String id) {
        ConnectionResponseProto responseProto = connectionsService.respondConnectionRequest(id, connectionRequestDto.getConnectingId(), true);
        if (responseProto == null || responseProto.getStatus().equals("Status 404"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }

    @PutMapping("users/{id}/connections/refuse")
    public ResponseEntity<HttpStatus> refuseConnectionRequest(@RequestBody @Valid ConnectionRequestDTO connectionRequestDto, @PathVariable String id) {
        ConnectionResponseProto responseProto = connectionsService.respondConnectionRequest(id, connectionRequestDto.getConnectingId(), false);
        if (responseProto == null || responseProto.getStatus().equals("Status 404"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }

}
