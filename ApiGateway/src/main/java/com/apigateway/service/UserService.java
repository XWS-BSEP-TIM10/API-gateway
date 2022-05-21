package com.apigateway.service;

import com.apigateway.dto.UpdateUserDTO;

import proto.*;

public interface UserService {

	UpdateUserResponseProto update(UpdateUserDTO dto);
	
	FindUserResponseProto find(String firstName, String lastName);

	UserNamesResponseProto getFirstAndLastName(String id);

	EmailResponseProto getEmail(String id);

	IdResponseProto getId(String email);

	UserResponseProto getById(String id);

	String getIdByToken(String token);
}
