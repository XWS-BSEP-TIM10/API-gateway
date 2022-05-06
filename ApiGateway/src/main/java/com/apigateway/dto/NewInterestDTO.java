package com.apigateway.dto;

public class NewInterestDTO {
	
	private String userId;

    private String description;

    public NewInterestDTO() {
    }
    
    

    public NewInterestDTO(String userId, String description) {
		super();
		this.userId = userId;
		this.description = description;
	}



	public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
