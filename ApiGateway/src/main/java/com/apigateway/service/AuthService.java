package com.apigateway.service;

import com.apigateway.dto.NewUserDTO;
import proto.NewUserResponseProto;

public interface AuthService {

    NewUserResponseProto signUp(NewUserDTO newUserDTO);
}
