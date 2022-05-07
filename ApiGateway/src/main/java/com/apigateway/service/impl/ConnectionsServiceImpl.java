package com.apigateway.service.impl;

import com.apigateway.service.ConnectionsService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import proto.ConnectionResponseProto;
import proto.ConnectionsGrpcServiceGrpc;
import proto.CreateConnectionRequestProto;
import proto.RespondConnectionRequestProto;

@Service
public class ConnectionsServiceImpl implements ConnectionsService {

    @GrpcClient("connectionsservice")
    private ConnectionsGrpcServiceGrpc.ConnectionsGrpcServiceBlockingStub stub;


    @Override
    public ConnectionResponseProto createConnection(String initiatorId, String connectingId) {
        CreateConnectionRequestProto connectionRequestProto = CreateConnectionRequestProto.newBuilder()
                .setInitiatorId(initiatorId)
                .setConnectingId(connectingId)
                .build();
        return this.stub.createConnection(connectionRequestProto);
    }

    @Override
    public ConnectionResponseProto respondConnectionRequest(String initiatorId, String connectingId, boolean approve) {
        RespondConnectionRequestProto connectionRequestProto = RespondConnectionRequestProto.newBuilder()
                .setInitiatorId(initiatorId)
                .setConnectingId(connectingId)
                .setApprove(approve)
                .build();
        return this.stub.respondConnection(connectionRequestProto);
    }
}
