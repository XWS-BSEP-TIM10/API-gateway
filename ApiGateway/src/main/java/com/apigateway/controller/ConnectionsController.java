package com.apigateway.controller;

import com.apigateway.dto.ConnectionRequestDTO;
import com.apigateway.dto.ConnectionStatusDto;
import com.apigateway.service.ConnectionsService;
import com.google.api.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import proto.ConnectionResponseProto;
import proto.ConnectionStatusResponseProto;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1")
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    public ConnectionsController(ConnectionsService connectionsService) {
        this.connectionsService = connectionsService;
    }
    
    @PreAuthorize("hasAuthority('CREATE_CONNECTION_PERMISSION')")
    @PostMapping(value = "connections")
    public ResponseEntity<HttpStatus> connect(@RequestBody @Valid ConnectionRequestDTO newConnectionRequestDTO) {
        ConnectionResponseProto connectionResponseProto = connectionsService.createConnection(newConnectionRequestDTO.getInitiatorId(), newConnectionRequestDTO.getReceiverId());
        if (connectionResponseProto.getStatus().equals("Status 400"))
            return ResponseEntity.badRequest().build();
        else if (connectionResponseProto.getStatus().equals("Status 200"))
            return ResponseEntity.ok().build();
        return ResponseEntity.internalServerError().build();
    }
    
    @PreAuthorize("hasAuthority('CHANGE_CONNECTION_STATUS_PERMISSION')")
    @PutMapping("connections/approve")
    public ResponseEntity<HttpStatus> approveConnectionRequest(@RequestBody @Valid ConnectionRequestDTO connectionRequestDto) {
        ConnectionResponseProto responseProto = connectionsService.respondConnectionRequest(connectionRequestDto.getInitiatorId(), connectionRequestDto.getReceiverId(), true);
        if (responseProto == null || responseProto.getStatus().equals("Status 404"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }
    
    @PreAuthorize("hasAuthority('CHANGE_CONNECTION_STATUS_PERMISSION')")
    @PutMapping("connections/refuse")
    public ResponseEntity<HttpStatus> refuseConnectionRequest(@RequestBody @Valid ConnectionRequestDTO connectionRequestDto) {
        ConnectionResponseProto responseProto = connectionsService.respondConnectionRequest(connectionRequestDto.getInitiatorId(), connectionRequestDto.getReceiverId(), false);
        if (responseProto == null || responseProto.getStatus().equals("Status 404"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('GET_CONNECTION_STATUS_PERMISSION')")
    @GetMapping("connections/status/{initiatorId}/{receiverId}")
    public ResponseEntity<ConnectionStatusDto> getConnectionStatus(@PathVariable String initiatorId, @PathVariable String receiverId) {
        ConnectionStatusResponseProto responseProto = connectionsService.getConnectionStatus(initiatorId, receiverId);
        return ResponseEntity.ok(new ConnectionStatusDto(responseProto.getConnectionStatus()));
    }

}
