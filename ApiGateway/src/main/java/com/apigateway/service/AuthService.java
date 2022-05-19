package com.apigateway.service;

import com.apigateway.dto.NewUserDTO;
import proto.*;

public interface AuthService {

    RecoverResponseProto recoverAccount(String id, String email);

    RecoveryPasswordResponseProto changePasswordRecovery(String newPassword, String repeatedNewPassword, String token);

    NewUserResponseProto signUp(NewUserDTO newUserDTO);

    LoginResponseProto login(String username, String password);

    VerifyAccountResponseProto verifyUserAccount(String verificationToken);

    ChangePasswordResponseProto changePassword(String userId, String oldPassword, String newPassword, String repeatedNewPassword);
}
