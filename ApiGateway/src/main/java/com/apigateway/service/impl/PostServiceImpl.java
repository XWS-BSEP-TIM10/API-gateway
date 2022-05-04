package com.apigateway.service.impl;

import com.apigateway.service.PostService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import proto.AddReactionProto;
import proto.AddReactionResponseProto;
import proto.AuthGrpcServiceGrpc;
import proto.PostGrpcServiceGrpc;

import javax.websocket.server.ServerEndpoint;

@Component
public class PostServiceImpl implements PostService {

    @GrpcClient("postgrpcservice")
    private PostGrpcServiceGrpc.PostGrpcServiceBlockingStub stub;

    @Override
    public AddReactionResponseProto addReaction(String postId, String userId, Boolean like) {
        AddReactionProto addReactionProto = AddReactionProto.newBuilder()
                                            .setPostId(postId)
                                            .setUserId(userId)
                                            .setLike(like)
                                            .build();
        return this.stub.addReaction(addReactionProto);
    }
}
