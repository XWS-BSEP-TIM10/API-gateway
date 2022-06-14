package com.apigateway.service;

import com.apigateway.dto.NewUserDTO;
import proto.APITokenResponseProto;
import proto.Change2FAStatusResponseProto;
import proto.ChangePasswordResponseProto;
import proto.LoginResponseProto;
import proto.NewUserResponseProto;
import proto.RecoveryPasswordResponseProto;
import proto.SendTokenResponseProto;
import proto.VerifyAccountResponseProto;

public interface AuthService {

    SendTokenResponseProto recoverAccount(String id, String email);

    RecoveryPasswordResponseProto changePasswordRecovery(String newPassword, String repeatedNewPassword, String token);

    NewUserResponseProto signUp(NewUserDTO newUserDTO);

    LoginResponseProto login(String username, String password, String code);

    VerifyAccountResponseProto verifyUserAccount(String verificationToken);

    ChangePasswordResponseProto changePassword(String userId, String oldPassword, String newPassword, String repeatedNewPassword);

    SendTokenResponseProto generateTokenPasswordless(String id, String email);

    LoginResponseProto passwordlessLogin(String verificationToken);

    LoginResponseProto refreshToken(String refreshToken);

    SendTokenResponseProto checkToken(String checkToken);

    APITokenResponseProto generateAPIToken(String userId);

    Change2FAStatusResponseProto change2FAStatus(String userId, boolean enable2FA);
}
