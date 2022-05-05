package com.apigateway.dto;

import proto.CommentProto;
import proto.PostProto;

import java.util.ArrayList;
import java.util.List;

public class PostsResponseDTO {
    private String id;
    private String text;
    private String ownerId;
    private List<String> likes;
    private List<String> dislikes;
    private List<NewCommentDTO> comments;
    private String creationDate;
    private String image;

    public PostsResponseDTO(PostProto post) {
        this.id = post.getPostId();
        this.text = post.getText();
        this.ownerId = post.getOwnerId();
        this.likes = post.getLikesList();
        this.dislikes = post.getDislikesList();
        this.comments = new ArrayList<>();
        for(CommentProto comment : post.getCommentsList()){
            this.comments.add(new NewCommentDTO(comment.getText()));
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

    public List<NewCommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<NewCommentDTO> comments) {
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
