package com.apigateway.service.impl;

import com.apigateway.service.ConnectionsService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import proto.ConnectionResponseProto;
import proto.ConnectionsGrpcServiceGrpc;
import proto.CreateConnectionRequestProto;
import proto.RespondConnectionRequestProto;

@Component
public class ConnectionsServiceImpl implements ConnectionsService {

    @GrpcClient("connectionsservice")
    private ConnectionsGrpcServiceGrpc.ConnectionsGrpcServiceBlockingStub stub;


    @Override
    public ConnectionResponseProto createConnection(String initiatorId, String receiverId) {
        CreateConnectionRequestProto connectionRequestProto = CreateConnectionRequestProto.newBuilder()
                .setInitiatorId(initiatorId)
                .setReceiverId(receiverId)
                .build();
        return this.stub.createConnection(connectionRequestProto);
    }

    @Override
    public ConnectionResponseProto respondConnectionRequest(String initiatorId, String receiverId, boolean approve) {
        RespondConnectionRequestProto connectionRequestProto = RespondConnectionRequestProto.newBuilder()
                .setInitiatorId(initiatorId)
                .setReceiverId(receiverId)
                .setApprove(approve)
                .build();
        return this.stub.respondConnection(connectionRequestProto);
    }
}
