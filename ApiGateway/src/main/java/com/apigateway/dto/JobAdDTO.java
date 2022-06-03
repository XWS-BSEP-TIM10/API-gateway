package com.apigateway.dto;

import proto.UserJobAdProto;

import java.util.List;

public class JobAdDTO {

    private String userId;

    private String firstName;

    private String lastName;

    private String title;

    private String position;

    private String description;

    private String company;

    private List<String> requirements;

    public JobAdDTO() {
    }

    public JobAdDTO(String userId, String firstName, String lastName, String title, String position, String description, String company, List<String> requirements) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.position = position;
        this.description = description;
        this.company = company;
        this.requirements = requirements;
    }

    public JobAdDTO(UserJobAdProto jobAd) {
        this.userId = jobAd.getUserId();
        this.firstName = jobAd.getFirstName();
        this.lastName = jobAd.getLastName();
        this.title = jobAd.getTitle();
        this.position = jobAd.getPosition();
        this.description = jobAd.getDescription();
        this.company = jobAd.getCompany();
        this.requirements = jobAd.getRequirementsList();
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTitle() {
        return title;
    }

    public String getPosition() {
        return position;
    }

    public String getDescription() {
        return description;
    }

    public String getCompany() {
        return company;
    }

    public List<String> getRequirements() {
        return requirements;
    }
}
