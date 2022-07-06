package com.apigateway.service;

import proto.BlockResponseProto;
import proto.ConnectionResponseProto;
import proto.ConnectionStatusResponseProto;
import proto.ConnectionsResponseProto;
import proto.CreateConnectionResponseProto;
import proto.MutualsResponseProto;
import proto.PendingResponseProto;
import proto.RecommendationsResponseProto;

public interface ConnectionsService {
    CreateConnectionResponseProto createConnection(String initiatorId, String connectingId);

    ConnectionResponseProto respondConnectionRequest(String initiatorId, String connectingId, boolean approve);

    ConnectionStatusResponseProto getConnectionStatus(String initiatorId, String receiverId);

    BlockResponseProto createBlock(String initiatorId, String connectingId);

    RecommendationsResponseProto getRecommendations(String userId);

    PendingResponseProto getPending(String userId);
    
    MutualsResponseProto getMutuals(String userId);
    
    ConnectionsResponseProto getFollowers (String userId);
}
