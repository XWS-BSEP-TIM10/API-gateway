package com.apigateway.service.impl;

import com.apigateway.dto.NewPostRequestDTO;
import com.apigateway.service.PostService;
import com.google.protobuf.ByteString;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import proto.*;

import java.io.IOException;

@Component
public class PostServiceImpl implements PostService {

    @GrpcClient("postgrpcservice")
    private PostGrpcServiceGrpc.PostGrpcServiceBlockingStub stub;

    @Override
    public AddPostResponseProto addPost(NewPostRequestDTO newPostRequestDTO, MultipartFile image) throws IOException {
        AddPostProto addPostProto = AddPostProto.newBuilder()
                                    .setOwnerId(newPostRequestDTO.getOwnerId())
                                    .setText(newPostRequestDTO.getText())
                                    .setImage(ByteString.copyFrom(image.getBytes()))
                                    .build();
        return this.stub.addPost(addPostProto);
    }

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
