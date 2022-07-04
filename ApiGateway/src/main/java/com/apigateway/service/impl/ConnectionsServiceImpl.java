package com.apigateway.service.impl;

import com.apigateway.service.ConnectionsService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import proto.BlockResponseProto;
import proto.ConnectionResponseProto;
import proto.ConnectionStatusProto;
import proto.ConnectionStatusResponseProto;
import proto.ConnectionsGrpcServiceGrpc;
import proto.CreateBlockRequestProto;
import proto.CreateConnectionRequestProto;
import proto.CreateConnectionResponseProto;
import proto.PendingRequestProto;
import proto.PendingResponseProto;
import proto.RecommendationsProto;
import proto.RecommendationsResponseProto;
import proto.RespondConnectionRequestProto;

@Component
public class ConnectionsServiceImpl implements ConnectionsService {

    @GrpcClient("connectionsservice")
    private ConnectionsGrpcServiceGrpc.ConnectionsGrpcServiceBlockingStub stub;


    @Override
    public CreateConnectionResponseProto createConnection(String initiatorId, String receiverId) {
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

    @Override
    public ConnectionStatusResponseProto getConnectionStatus(String initiatorId, String receiverId) {
        ConnectionStatusProto connectionStatusProto = ConnectionStatusProto.newBuilder()
                .setInitiatorId(initiatorId)
                .setReceiverId(receiverId)
                .build();
        return this.stub.getConnectionStatus(connectionStatusProto);
    }


    @Override
    public BlockResponseProto createBlock(String initiatorId, String receiverId) {
        CreateBlockRequestProto createBlockRequestProto = CreateBlockRequestProto.newBuilder()
                .setInitiatorId(initiatorId)
                .setReceiverId(receiverId)
                .build();
        return this.stub.createBlock(createBlockRequestProto);
    }

    @Override
    public RecommendationsResponseProto getRecommendations(String userId) {
        RecommendationsProto recommendationsProto = RecommendationsProto.newBuilder()
                .setUserId(userId)
                .build();
        return this.stub.getRecommendations(recommendationsProto);
    }

    @Override
    public PendingResponseProto getPending(String userId) {
        PendingRequestProto pendingProto = PendingRequestProto.newBuilder()
                .setUserId(userId)
                .build();
        return this.stub.getPending(pendingProto);
    }
}
