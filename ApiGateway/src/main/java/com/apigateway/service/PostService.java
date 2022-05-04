package com.apigateway.service;

import com.apigateway.dto.NewPostRequestDTO;
import org.springframework.web.multipart.MultipartFile;
import proto.AddPostResponseProto;
import proto.AddReactionResponseProto;
import proto.CommentPostResponseProto;
import proto.UserPostsResponseProto;

import java.io.IOException;

public interface PostService {
    AddPostResponseProto addPost(NewPostRequestDTO newPostRequestDTO, MultipartFile image) throws IOException;
    AddReactionResponseProto addReaction(String postId, String userId, Boolean like);

    CommentPostResponseProto addComment(String postId, String userId, String text);

    UserPostsResponseProto getPostsFromUser(String id);
}
