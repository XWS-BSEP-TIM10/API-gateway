package com.apigateway.controller;

import com.apigateway.dto.BlockRequestDTO;
import com.apigateway.dto.ConnectionRequestDTO;
import com.apigateway.dto.ConnectionStatusDto;
import com.apigateway.service.ConnectionsService;
import com.apigateway.service.LoggerService;
import com.apigateway.service.impl.LoggerServiceImpl;
import io.grpc.StatusRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proto.BlockResponseProto;
import proto.ConnectionResponseProto;
import proto.ConnectionStatusResponseProto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1")
public class ConnectionsController {

    private final ConnectionsService connectionsService;
    private final LoggerService loggerService;

    public ConnectionsController(ConnectionsService connectionsService) {
        this.connectionsService = connectionsService;
        this.loggerService = new LoggerServiceImpl(this.getClass());
    }

    @PreAuthorize("hasAuthority('CREATE_CONNECTION_PERMISSION')")
    @PostMapping(value = "connections")
    public ResponseEntity<HttpStatus> connect(@RequestBody @Valid ConnectionRequestDTO newConnectionRequestDTO, HttpServletRequest request) {
        try {
            ConnectionResponseProto connectionResponseProto = connectionsService.createConnection(newConnectionRequestDTO.getInitiatorId(), newConnectionRequestDTO.getReceiverId());
            if (connectionResponseProto.getStatus().equals("Status 400"))
                return ResponseEntity.badRequest().build();
            else if (connectionResponseProto.getStatus().equals("Status 200"))
                return ResponseEntity.ok().build();
            return ResponseEntity.internalServerError().build();
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('CREATE_BLOCK_PERMISSION')")
    @PostMapping(value = "connections/block")
    public ResponseEntity<HttpStatus> block(@RequestBody @Valid BlockRequestDTO blockRequestDTO, HttpServletRequest request) {
        try {
            BlockResponseProto blockResponseProto = connectionsService.createBlock(blockRequestDTO.getInitiatorId(), blockRequestDTO.getReceiverId());
            if (blockResponseProto.getStatus().equals("Status 400"))
                return ResponseEntity.badRequest().build();
            else if (blockResponseProto.getStatus().equals("Status 200"))
                return ResponseEntity.ok().build();
            return ResponseEntity.internalServerError().build();
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('CHANGE_CONNECTION_STATUS_PERMISSION')")
    @PutMapping("connections/approve")
    public ResponseEntity<HttpStatus> approveConnectionRequest(@RequestBody @Valid ConnectionRequestDTO connectionRequestDto, HttpServletRequest request) {
        try {
            ConnectionResponseProto responseProto = connectionsService.respondConnectionRequest(connectionRequestDto.getInitiatorId(), connectionRequestDto.getReceiverId(), true);
            if (responseProto == null || responseProto.getStatus().equals("Status 404"))
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok().build();
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('CHANGE_CONNECTION_STATUS_PERMISSION')")
    @PutMapping("connections/refuse")
    public ResponseEntity<HttpStatus> refuseConnectionRequest(@RequestBody @Valid ConnectionRequestDTO connectionRequestDto, HttpServletRequest request) {
        try {
            ConnectionResponseProto responseProto = connectionsService.respondConnectionRequest(connectionRequestDto.getInitiatorId(), connectionRequestDto.getReceiverId(), false);
            if (responseProto == null || responseProto.getStatus().equals("Status 404"))
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok().build();
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('GET_CONNECTION_STATUS_PERMISSION')")
    @GetMapping("connections/status/{initiatorId}/{receiverId}")
    public ResponseEntity<ConnectionStatusDto> getConnectionStatus(@PathVariable String initiatorId, @PathVariable String receiverId, HttpServletRequest request) {
        try {
            ConnectionStatusResponseProto responseProto = connectionsService.getConnectionStatus(initiatorId, receiverId);
            return ResponseEntity.ok(new ConnectionStatusDto(responseProto.getConnectionStatus()));
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

}
