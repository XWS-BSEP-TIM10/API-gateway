package com.apigateway.dto;

import proto.UpdateUserResponseProto;

public class UpdateResponseDTO {

	 private boolean success;
	    private String message;

	    public UpdateResponseDTO() {
	    }

	    public UpdateResponseDTO(boolean success, String message) {
	        this.success = success;
	        this.message = message;
	    }
	    
	    public UpdateResponseDTO(UpdateUserResponseProto proto) {
	    	this.success = proto.getSuccess();
	    	this.message = proto.getMessage();
	    }
	    

	    public boolean isSuccess() {
	        return success;
	    }

	    public String getMessage() {
	        return message;
	    }
}
