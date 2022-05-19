package com.apigateway.service;

import proto.ConnectionResponseProto;
import proto.ConnectionStatusResponseProto;

public interface ConnectionsService {
    ConnectionResponseProto createConnection(String initiatorId, String connectingId);

    ConnectionResponseProto respondConnectionRequest(String initiatorId, String connectingId, boolean approve);

    ConnectionStatusResponseProto getConnectionStatus(String initiatorId, String receiverId);
}
