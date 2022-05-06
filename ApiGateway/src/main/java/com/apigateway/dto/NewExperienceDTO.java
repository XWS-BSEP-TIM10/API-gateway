package com.apigateway.dto;

public class NewExperienceDTO {
	
	 private String userId;

	    private String institution;

	    private String position;

	    private String fromDate;

	    private String toDate;

	    private String description;

	    private String type;

	    public NewExperienceDTO() {
	    }

	    
	    
	    public NewExperienceDTO(String userId, String institution, String position, String fromDate, String toDate,
				String description, String type) {
			super();
			this.userId = userId;
			this.institution = institution;
			this.position = position;
			this.fromDate = fromDate;
			this.toDate = toDate;
			this.description = description;
			this.type = type;
		}



		public String getUserId() {
	        return userId;
	    }

	    public void setUserId(String userId) {
	        this.userId = userId;
	    }

	    public String getInstitution() {
	        return institution;
	    }

	    public void setInstitution(String institution) {
	        this.institution = institution;
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
