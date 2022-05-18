package com.apigateway.dto;

import proto.CommentProto;
import proto.PostProto;
import proto.UserNamesResponseProto;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.List;

public class PostsResponseDTO {
    private String id;
    private String text;
    private String ownerId;
    private String firstName;
    private String lastName;
    private List<String> likes;
    private List<String> dislikes;
    private List<CommentDTO> comments;
    private String creationDate;
    private String image;

    public PostsResponseDTO(PostProto post, UserNamesResponseProto names) {
        this.id = post.getPostId();
        this.text = post.getText();
        this.ownerId = post.getOwnerId();
        this.firstName = names.getFirstName();
        this.lastName = names.getLastName();
        this.likes = post.getLikesList();
        this.dislikes = post.getDislikesList();
        this.comments = new ArrayList<>();
        for(CommentProto comment : post.getCommentsList()){
            this.comments.add(new CommentDTO(comment.getText(), comment.getUserId()));
        }
        this.creationDate = post.getCreationDate();
        this.image = post.getImage();
    }


    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public List<String> getDislikes() {
        return dislikes;
    }

    public void setDislikes(List<String> dislikes) {
        this.dislikes = dislikes;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
