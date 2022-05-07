package com.apigateway.service;

import com.apigateway.dto.NewUserDTO;
import proto.LoginResponseProto;
import proto.NewUserResponseProto;

public interface AuthService {

    NewUserResponseProto signUp(NewUserDTO newUserDTO);

    LoginResponseProto login(String username, String password);
}
