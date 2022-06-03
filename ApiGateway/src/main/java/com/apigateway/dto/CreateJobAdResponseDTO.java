package com.apigateway.dto;

import proto.JobAdProto;
import proto.JobAdResponseProto;

import java.util.List;

public class CreateJobAdResponseDTO {

    private String id;

    private String title;

    private String position;

    private String description;

    private String company;

    private List<String> requirements;

    public CreateJobAdResponseDTO(JobAdResponseProto jobAd) {
        this.id = jobAd.getId();
        this.title = jobAd.getTitle();
        this.position = jobAd.getPosition();
        this.description = jobAd.getDescription();
        this.company = jobAd.getCompany();
        this.requirements = jobAd.getRequirementsList();
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

    public String getId() {
        return id;
    }
}
