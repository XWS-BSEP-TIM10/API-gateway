package com.apigateway.controller;

import com.apigateway.dto.NewPostRequestDTO;
import com.apigateway.dto.NewPostResponseDTO;
import com.apigateway.dto.ReactionDTO;
import com.apigateway.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import proto.AddPostResponseProto;
import proto.AddReactionResponseProto;

import javax.validation.Valid;
import java.io.IOException;

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

}
