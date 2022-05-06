package com.apigateway.dto;

import proto.NewExperienceResponseProto;
import proto.UpdateExperienceResponseProto;

public class ExperienceDTO {

	 private Long id;

	    private String position;

	    private String fromDate;

	    private String toDate;

	    private String description;

	    private String type;

	    public ExperienceDTO() {
	    }
	    
	    public ExperienceDTO(NewExperienceResponseProto proto) {
	    	this.id = proto.getId();
	    	this.position = proto.getPosition();
	    	this.fromDate = proto.getFromDate();
	    	this.toDate = proto.getToDate();
	    	this.description = proto.getDescription();
	    	this.type = proto.getType();
	    }
	    
	    public ExperienceDTO(UpdateExperienceResponseProto proto) {
	    	this.id = proto.getId();
	    	this.position = proto.getPosition();
	    	this.fromDate = proto.getFromDate();
	    	this.toDate = proto.getToDate();
	    	this.description = proto.getDescription();
	    	this.type = proto.getType();
	    }


	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getPosition() {
	        return position;
	    }

	    public void setPosition(String position) {
	        this.position = position;
	    }

	    public String getFromDate() {
	        return fromDate;
	    }

	    public void setFromDate(String fromDate) {
	        this.fromDate = fromDate;
	    }

	    public String getToDate() {
	        return toDate;
	    }

	    public void setToDate(String toDate) {
	        this.toDate = toDate;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }

	    public String getType() {
	        return type;
	    }

	    public void setType(String type) {
	        this.type = type;
	    }
	    
}
