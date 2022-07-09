package com.apigateway.controller;

import com.apigateway.dto.ChatNotificationDTO;
import com.apigateway.dto.CommentDTO;
import com.apigateway.dto.CommentResponseDTO;
import com.apigateway.dto.NewCommentDTO;
import com.apigateway.dto.NewPostRequestDTO;
import com.apigateway.dto.NewPostResponseDTO;
import com.apigateway.dto.PostsResponseDTO;
import com.apigateway.dto.ReactionDTO;
import com.apigateway.dto.RemoveReactionDTO;
import com.apigateway.mapper.PostMapper;
import com.apigateway.model.User;
import com.apigateway.service.ConnectionsService;
import com.apigateway.service.LoggerService;
import com.apigateway.service.NotificationService;
import com.apigateway.service.PostService;
import com.apigateway.service.UserService;
import com.apigateway.service.impl.LoggerServiceImpl;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class PostController {

    private final PostService postService;
    private SimpMessagingTemplate messagingTemplate;

    private final UserService userService;

    private final LoggerService loggerService;
    
    private final ConnectionsService connectionsService;
    
    private final NotificationService notificationService;

    @Autowired
    public PostController(PostService postService, UserService userService, SimpMessagingTemplate messagingTemplate,ConnectionsService connectionsService,NotificationService notificationService) {
        this.postService = postService;
        this.userService = userService;
        this.loggerService = new LoggerServiceImpl(this.getClass());
        this.messagingTemplate = messagingTemplate;
        this.connectionsService = connectionsService;
        this.notificationService = notificationService;
    }

    @PreAuthorize("hasAuthority('CREATE_POST_PERMISSION')")
    @PostMapping(value = "posts")
    public ResponseEntity<NewPostResponseDTO> addPost(@RequestPart("post") @Valid NewPostRequestDTO newPostRequestDTO,
                                                      @RequestPart("image") MultipartFile image, HttpServletRequest request) throws IOException {
        try {
            AddPostResponseProto addPostResponseProto = postService.addPost(newPostRequestDTO, image);
            ConnectionsResponseProto connectionsResponseProto= connectionsService.getFollowers(newPostRequestDTO.getOwnerId());
            UserNamesResponseProto userNamesResponseProto = userService.getFirstAndLastName(newPostRequestDTO.getOwnerId());
            NotificationResponseProto notificationResponseProto = notificationService.addPostNotification(connectionsResponseProto.getConnectionsList(),userNamesResponseProto.getFirstName()+" "+userNamesResponseProto.getLastName());
            NewPostResponseDTO newPostResponseDTO = new NewPostResponseDTO(addPostResponseProto.getId());
            for(String tempUserId: connectionsResponseProto.getConnectionsList()) {
                UserResponseProto userResponseProto = userService.getById(tempUserId);
                if(userResponseProto.getUser().getMutePostNotifications())
                    continue;
            	messagingTemplate.convertAndSendToUser(
                        tempUserId,"/queue/posts",
                        new ChatNotificationDTO(
                        		newPostRequestDTO.getOwnerId(),newPostRequestDTO.getOwnerId(),userNamesResponseProto.getFirstName()+" "+userNamesResponseProto.getLastName()));
            }
            return ResponseEntity.ok(newPostResponseDTO);
            
            
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('REACT_POST_PERMISSION')")
    @PutMapping(value = "posts/{postId}/reaction")
    public ResponseEntity<HttpStatus> addReaction(@PathVariable String postId, @RequestBody ReactionDTO dto, HttpServletRequest request) {
        try {
            AddReactionResponseProto addReactionResponseProto = postService.addReaction(postId, dto.getUserId(), dto.isLike());
            if (addReactionResponseProto.getStatus().equals("Status 400"))
                return ResponseEntity.badRequest().build();
            return ResponseEntity.ok().build();
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('REACT_POST_PERMISSION')")
    @PutMapping(value = "posts/{postId}/remove-reaction")
    public ResponseEntity<HttpStatus> removeReaction(@PathVariable String postId, @RequestBody RemoveReactionDTO dto, HttpServletRequest request) {
        try {
            RemoveReactionResponseProto responseProto = postService.removeReaction(postId, dto.getUserId());
            if (responseProto.getStatus().equals("Status 404"))
                return ResponseEntity.notFound().build();
            if (responseProto.getStatus().equals("Status 400"))
                return ResponseEntity.badRequest().build();
            return ResponseEntity.ok().build();
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('COMMENT_POST_PERMISSION')")
    @PostMapping(value = "posts/{postId}/comment")
    public ResponseEntity<CommentResponseDTO> commentPost(@PathVariable String postId, @RequestBody NewCommentDTO dto, HttpServletRequest request) {
        try {
            CommentPostResponseProto commentPostResponseProto = postService.addComment(postId, dto.getUserId(), dto.getText());
            UserNamesResponseProto userNamesResponseProto = userService.getFirstAndLastName(dto.getUserId());
            if (commentPostResponseProto.getStatus().equals("Status 400")) return ResponseEntity.badRequest().build();
            CommentResponseDTO commentResponseDTO = new CommentResponseDTO(commentPostResponseProto.getComment(), userNamesResponseProto);
            return ResponseEntity.ok(commentResponseDTO);
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('GET_POST_PERMISSION')")
    @GetMapping(value = "users/{id}/posts")
    public ResponseEntity<List<PostsResponseDTO>> getUserPosts(@PathVariable String id, HttpServletRequest request) {
        try {
            UserPostsResponseProto posts = postService.getPostsFromUser(id);
            List<PostsResponseDTO> responseDTOS = new ArrayList<>();
            for (PostProto post : posts.getPostsList()) {
                UserNamesResponseProto userNamesResponseProto = userService.getFirstAndLastName(post.getOwnerId());
                PostsResponseDTO postsResponseDTO = PostMapper.toDTO(post, userNamesResponseProto);
                for (CommentProto comment : post.getCommentsList()) {
                    userNamesResponseProto = userService.getFirstAndLastName(comment.getUserId());
                    postsResponseDTO.getComments().add(new CommentDTO(comment, userNamesResponseProto));
                }
                responseDTOS.add(postsResponseDTO);
            }
            return ResponseEntity.ok(responseDTOS);
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('GET_FEED_PERMISSION')")
    @GetMapping(value = "users/{id}/feed")
    public ResponseEntity<List<PostsResponseDTO>> userFeed(@PathVariable String id, HttpServletRequest request) {
        try {
            UserPostsResponseProto posts = postService.getFeed(id);
            List<PostsResponseDTO> responseDTOS = new ArrayList<>();
            UserNamesResponseProto userNamesResponseProto;
            for (PostProto post : posts.getPostsList()) {
                userNamesResponseProto = userService.getFirstAndLastName(post.getOwnerId());
                PostsResponseDTO postsResponseDTO = PostMapper.toDTO(post, userNamesResponseProto);
                for (CommentProto comment : post.getCommentsList()) {
                    userNamesResponseProto = userService.getFirstAndLastName(comment.getUserId());
                    postsResponseDTO.getComments().add(new CommentDTO(comment, userNamesResponseProto));
                }
                responseDTOS.add(postsResponseDTO);
            }
            return ResponseEntity.ok(responseDTOS);
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }

    }

    @PreAuthorize("hasAuthority('GET_EVENTS_PERMISSION')")
    @GetMapping(value = "/posts/events")
    public ResponseEntity<List<String>> getEvents() {
        List<String> events = new ArrayList<>();
        for(String event : postService.getEvents().getEventsList()){
            events.add(event);
        }
        return ResponseEntity.ok(events);
    }
}
