package com.apigateway.dto;

import proto.NewInterestResponseProto;

public class InterestDTO {
	
	 private Long id;

	    private String description;

	    public InterestDTO() {
	    }
	    
	    public InterestDTO(NewInterestResponseProto proto) {
	    	this.id = proto.getId();
	    	this.description = proto.getDescription();
	    }

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }

}
