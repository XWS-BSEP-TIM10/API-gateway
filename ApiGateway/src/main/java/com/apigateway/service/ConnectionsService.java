package com.apigateway.service;

import proto.ConnectionResponseProto;

public interface ConnectionsService {
    ConnectionResponseProto createConnection(String initiatorId, String connectingId);

    ConnectionResponseProto respondConnectionRequest(String initiatorId, String connectingId, boolean approve);
}
