package com.apigateway.controller;

import com.apigateway.dto.*;
import com.apigateway.mapper.PostMapper;
import com.apigateway.service.PostService;
import com.apigateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import proto.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }
    
    @PreAuthorize("hasAuthority('CREATE_POST_PERMISSION')")
    @PostMapping(value = "posts")
    public ResponseEntity<NewPostResponseDTO> addPost(@RequestPart("post") @Valid NewPostRequestDTO newPostRequestDTO,
                                                      @RequestPart("image") MultipartFile image) throws IOException {
        AddPostResponseProto addPostResponseProto = postService.addPost(newPostRequestDTO, image);
        NewPostResponseDTO newPostResponseDTO = new NewPostResponseDTO(addPostResponseProto.getId());
        return ResponseEntity.ok(newPostResponseDTO);
    }
    
    @PreAuthorize("hasAuthority('REACT_POST_PERMISSION')")
    @PutMapping(value = "posts/{postId}/reaction")
    public ResponseEntity<HttpStatus> addReaction(@PathVariable String postId, @RequestBody ReactionDTO dto) {
        AddReactionResponseProto addReactionResponseProto = postService.addReaction(postId, dto.getUserId(), dto.isLike());
        if (addReactionResponseProto.getStatus().equals("Status 400"))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
    }
    
    @PreAuthorize("hasAuthority('REACT_POST_PERMISSION')")
    @PutMapping(value = "posts/{postId}/remove-reaction")
    public ResponseEntity<HttpStatus> removeReaction(@PathVariable String postId, @RequestBody RemoveReactionDTO dto) {
        RemoveReactionResponseProto responseProto = postService.removeReaction(postId, dto.getUserId());
        if (responseProto.getStatus().equals("Status 404"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }
    
    @PreAuthorize("hasAuthority('COMMENT_POST_PERMISSION')")
    @PostMapping(value = "posts/{postId}/comment")
    public ResponseEntity<CommentResponseDTO> commentPost(@PathVariable String postId, @RequestBody NewCommentDTO dto) {
        CommentPostResponseProto commentPostResponseProto = postService.addComment(postId, dto.getUserId(), dto.getText());
        UserNamesResponseProto userNamesResponseProto = userService.getFirstAndLastName(dto.getUserId());
        if (commentPostResponseProto.getStatus().equals("Status 400")) return ResponseEntity.badRequest().build();
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO(commentPostResponseProto.getComment(), userNamesResponseProto);
        return ResponseEntity.ok(commentResponseDTO);
    }
    
    @PreAuthorize("hasAuthority('GET_POST_PERMISSION')")
    @GetMapping(value = "users/{id}/posts")
    public ResponseEntity<List<PostsResponseDTO>> getUserPosts(@PathVariable String id) {
        UserPostsResponseProto posts = postService.getPostsFromUser(id);
        List<PostsResponseDTO> responseDTOS = new ArrayList<>();
        for (PostProto post : posts.getPostsList()) {
            UserNamesResponseProto userNamesResponseProto = userService.getFirstAndLastName(post.getOwnerId());
            PostsResponseDTO postsResponseDTO = PostMapper.toDTO(post, userNamesResponseProto);
            for(CommentProto comment : post.getCommentsList()){
                userNamesResponseProto = userService.getFirstAndLastName(comment.getUserId());
                postsResponseDTO.getComments().add(new CommentDTO(comment, userNamesResponseProto));
            }
            responseDTOS.add(postsResponseDTO);
        }
        return ResponseEntity.ok(responseDTOS);
    }
    
    @PreAuthorize("hasAuthority('GET_FEED_PERMISSION')")
    @GetMapping(value = "users/{id}/feed")
    public ResponseEntity<List<PostsResponseDTO>> userFeed(@PathVariable String id) {
        UserPostsResponseProto posts = postService.getFeed(id);
        List<PostsResponseDTO> responseDTOS = new ArrayList<>();
        UserNamesResponseProto userNamesResponseProto;
        for (PostProto post : posts.getPostsList()) {
            userNamesResponseProto = userService.getFirstAndLastName(post.getOwnerId());
            PostsResponseDTO postsResponseDTO = PostMapper.toDTO(post, userNamesResponseProto);
            for(CommentProto comment : post.getCommentsList()){
                userNamesResponseProto = userService.getFirstAndLastName(comment.getUserId());
                postsResponseDTO.getComments().add(new CommentDTO(comment, userNamesResponseProto));
            }
            responseDTOS.add(postsResponseDTO);
        }

        return ResponseEntity.ok(responseDTOS);

    }
}
