package com.apigateway.service;

import com.apigateway.dto.NewUserDTO;
import proto.ChangePasswordResponseProto;
import proto.LoginResponseProto;
import proto.NewUserResponseProto;
import proto.VerifyAccountResponseProto;

public interface AuthService {

    NewUserResponseProto signUp(NewUserDTO newUserDTO);

    LoginResponseProto login(String username, String password);

    VerifyAccountResponseProto verifyUserAccount(String verificationToken);

    ChangePasswordResponseProto changePassword(String userId, String oldPassword, String newPassword, String repeatedNewPassword);
}
