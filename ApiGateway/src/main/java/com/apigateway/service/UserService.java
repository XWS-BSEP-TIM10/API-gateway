package com.apigateway.service;

import com.apigateway.dto.UpdateUserDTO;

import proto.FindUserResponseProto;
import proto.UpdateUserResponseProto;
import proto.UserNamesResponseProto;

public interface UserService {

	UpdateUserResponseProto update(UpdateUserDTO dto);
	
	FindUserResponseProto find(String firstName, String lastName);

	UserNamesResponseProto getFirstAndLastName(String id);
}
