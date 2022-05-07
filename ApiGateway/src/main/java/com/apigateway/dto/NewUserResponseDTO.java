package com.apigateway.dto;

public class NewUserResponseDTO {

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String gender;

    private String dateOfBirth;

    private String username;

    private String password;

    private String biography;

    public NewUserResponseDTO() {
    }

    public NewUserResponseDTO(String id, NewUserDTO dto) {
        this.id = id;
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.email = dto.getEmail();
        this.phoneNumber = dto.getPhoneNumber();
        this.gender = dto.getGender();
        this.dateOfBirth = dto.getDateOfBirth();
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.biography = dto.getBiography();
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBiography() {
        return biography;
    }
}
