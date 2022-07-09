package com.apigateway.dto;

import proto.ExperienceProto;
import proto.InterestProto;
import proto.UserProto;

import java.util.HashSet;
import java.util.Set;


public class UserDto {

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String gender;

    private String dateOfBirth;

    private String username;

    private String biography;

    private boolean profilePublic;

    private Set<ExperienceDTO> experiences;

    private Set<InterestDTO> interests;

    private boolean muteConnectionsNotifications;

    private boolean muteMessageNotifications;

    private boolean mutePostNotifications;

    public UserDto() {

    }

    public UserDto(UserProto userProto) {
        this.id = userProto.getUuid();
        this.firstName = userProto.getFirstName();
        this.lastName = userProto.getLastName();
        this.email = userProto.getEmail();
        this.phoneNumber = userProto.getPhoneNumber();
        this.gender = userProto.getGender();
        this.dateOfBirth = userProto.getDateOfBirth();
        this.username = userProto.getUsername();
        this.biography = userProto.getBiography();
        this.experiences = new HashSet<>();
        this.interests = new HashSet<>();
        for (ExperienceProto experienceProto : userProto.getExperiencesList()) {
            this.experiences.add(new ExperienceDTO(experienceProto));
        }

        for (InterestProto interestProto : userProto.getInterestsList()) {
            this.interests.add(new InterestDTO(interestProto));
        }
        this.profilePublic = userProto.getProfilePublic();
        this.muteMessageNotifications = userProto.getMuteMessageNotifications();
        this.muteConnectionsNotifications = userProto.getMuteConnectionsNotifications();
        this.mutePostNotifications = userProto.getMutePostNotifications();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Set<ExperienceDTO> getExperiences() {
        return experiences;
    }

    public void setExperiences(Set<ExperienceDTO> experiences) {
        this.experiences = experiences;
    }

    public Set<InterestDTO> getInterests() {
        return interests;
    }

    public void setInterests(Set<InterestDTO> interests) {
        this.interests = interests;
    }

    public boolean isProfilePublic() {
        return profilePublic;
    }

    public boolean isMuteConnectionsNotifications() {
        return muteConnectionsNotifications;
    }

    public boolean isMuteMessageNotifications() {
        return muteMessageNotifications;
    }

    public boolean isMutePostNotifications() {
        return mutePostNotifications;
    }
}
