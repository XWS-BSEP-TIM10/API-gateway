package com.apigateway.controller;

import com.apigateway.dto.*;
import com.apigateway.mapper.PostMapper;
import com.apigateway.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import proto.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(value = "posts")
    public ResponseEntity<NewPostResponseDTO> addPost(@RequestPart("post") @Valid NewPostRequestDTO newPostRequestDTO,
                                                      @RequestPart("image") MultipartFile image) throws IOException {
        AddPostResponseProto addPostResponseProto = postService.addPost(newPostRequestDTO, image);
        NewPostResponseDTO newPostResponseDTO = new NewPostResponseDTO(addPostResponseProto.getId());
        return ResponseEntity.ok(newPostResponseDTO);
    }

    @PutMapping(value = "users/{userId}/posts/{postId}/reaction")
    public ResponseEntity<HttpStatus> addReaction(@PathVariable String userId, @PathVariable String postId, @RequestBody ReactionDTO dto) {
        AddReactionResponseProto addReactionResponseProto = postService.addReaction(postId, userId, dto.isLike());
        if (addReactionResponseProto.getStatus().equals("Status 400"))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "users/{userId}/posts/{postId}/comment")
    public ResponseEntity<CommentResponseDTO> commentPost(@PathVariable String userId, @PathVariable String postId, @RequestBody NewCommentDTO dto) {
        CommentPostResponseProto commentPostResponseProto = postService.addComment(postId, userId, dto.getText());
        if (commentPostResponseProto.getStatus().equals("Status 400")) return ResponseEntity.badRequest().build();
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO(commentPostResponseProto.getComment());
        return ResponseEntity.ok(commentResponseDTO);
    }

    @GetMapping(value = "users/{id}/posts")
    public ResponseEntity<List<PostsResponseDTO>> getUserPosts(@PathVariable String id) {
        UserPostsResponseProto posts = postService.getPostsFromUser(id);
        List<PostsResponseDTO> responseDTOS = new ArrayList<>();
        for (PostProto post : posts.getPostsList()) {
            PostsResponseDTO postsResponseDTO = PostMapper.toDTO(post);
            responseDTOS.add(postsResponseDTO);
        }
        return ResponseEntity.ok(responseDTOS);
    }
}
