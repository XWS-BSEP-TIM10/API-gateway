package com.apigateway.service.impl;

import com.apigateway.dto.NewPostRequestDTO;
import com.apigateway.service.PostService;
import com.google.protobuf.ByteString;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import proto.AddPostProto;
import proto.AddPostResponseProto;
import proto.AddReactionProto;
import proto.AddReactionResponseProto;
import proto.CommentPostProto;
import proto.CommentPostResponseProto;
import proto.PostGrpcServiceGrpc;
import proto.RemoveReactionProto;
import proto.RemoveReactionResponseProto;
import proto.UserPostsProto;
import proto.UserPostsResponseProto;

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

    @Override
    public RemoveReactionResponseProto removeReaction(String postId, String userId) {
        RemoveReactionProto reactionProto = RemoveReactionProto.newBuilder()
                .setPostId(postId)
                .setUserId(userId)
                .build();
        return this.stub.removeReaction(reactionProto);
    }

    @Override
    public CommentPostResponseProto addComment(String postId, String userId, String text) {
        CommentPostProto commentPostProto = CommentPostProto.newBuilder()
                .setPostId(postId)
                .setUserId(userId)
                .setComment(text)
                .build();
        return this.stub.commentPost(commentPostProto);
    }

    @Override
    public UserPostsResponseProto getPostsFromUser(String id) {
        UserPostsProto userPostsProto = UserPostsProto.newBuilder().setUserId(id).build();
        return this.stub.getUserPosts(userPostsProto);
    }

    @Override
    public UserPostsResponseProto getFeed(String id) {
        UserPostsProto userPostsProto = UserPostsProto.newBuilder().setUserId(id).build();
        return this.stub.getUserFeed(userPostsProto);
    }
}
