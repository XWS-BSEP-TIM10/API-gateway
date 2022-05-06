package com.apigateway.service;

import com.apigateway.dto.NewUserDTO;

import proto.UpdateUserResponseProto;

public interface UserService {

	UpdateUserResponseProto update(NewUserDTO dto);
	
}
