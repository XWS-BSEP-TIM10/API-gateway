package com.apigateway.dto;

import proto.CommentProto;
import proto.UserNamesResponseProto;

public class CommentDTO {
    private String text;
    private String ownerId;
    private String firstName;
    private String lastName;

    public CommentDTO() {
    }

    public CommentDTO(CommentProto commentProto, UserNamesResponseProto userNames) {
        this.text = commentProto.getText();
        this.ownerId = commentProto.getUserId();
        this.firstName = userNames.getFirstName();
        this.lastName = userNames.getLastName();
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
}
