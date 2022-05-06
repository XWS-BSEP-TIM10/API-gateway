package com.apigateway.service;

import com.apigateway.dto.NewUserDTO;

import proto.FindUserResponseProto;
import proto.UpdateUserResponseProto;

public interface UserService {

	UpdateUserResponseProto update(NewUserDTO dto);
	
	FindUserResponseProto find(String firstName, String lastName);
}
