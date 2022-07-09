package com.apigateway.service;

import proto.*;

public interface ConnectionsService {

    ConnectionsEventResponseProto getEvents();

    CreateConnectionResponseProto createConnection(String initiatorId, String connectingId);

    ConnectionResponseProto respondConnectionRequest(String initiatorId, String connectingId, boolean approve);

    ConnectionStatusResponseProto getConnectionStatus(String initiatorId, String receiverId);

    BlockResponseProto createBlock(String initiatorId, String connectingId);

    RecommendationsResponseProto getRecommendations(String userId);

    PendingResponseProto getPending(String userId);
    
    MutualsResponseProto getMutuals(String userId);
    
    ConnectionsResponseProto getFollowers (String userId);

    ConnectionsResponseProto getConnections(String userId);

}
