package com.apigateway.dto;

import proto.UserNamesResponseProto;

public class CommentResponseDTO {

    private String text;

    private String firstName;

    private String lastName;

    public CommentResponseDTO() {
    }

    public CommentResponseDTO(String text) {
        this.text = text;
    }

    public CommentResponseDTO(String comment, UserNamesResponseProto userNames) {
        this.text = comment;
        this.firstName = userNames.getFirstName();
        this.lastName = userNames.getLastName();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
