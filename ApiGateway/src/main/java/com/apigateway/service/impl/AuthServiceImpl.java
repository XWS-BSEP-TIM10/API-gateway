package com.apigateway.service.impl;

import com.apigateway.dto.NewUserDTO;
import com.apigateway.service.AuthService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import proto.*;

@Component
public class AuthServiceImpl implements AuthService {

    @GrpcClient("authgrpcservice")
    private AuthGrpcServiceGrpc.AuthGrpcServiceBlockingStub stub;

    @Override
    public NewUserResponseProto signUp(NewUserDTO newUserDTO) {
        NewUserProto newUserProto = NewUserProto.newBuilder()
                .setId(newUserDTO.getId())
                .setFirstName(newUserDTO.getFirstName())
                .setLastName(newUserDTO.getLastName())
                .setEmail(newUserDTO.getEmail())
                .setPhoneNumber(newUserDTO.getPhoneNumber())
                .setGender(newUserDTO.getGender())
                .setDateOfBirth(newUserDTO.getDateOfBirth())
                .setUsername(newUserDTO.getUsername())
                .setPassword(newUserDTO.getPassword())
                .setBiography(newUserDTO.getBiography())
                .build();
        return this.stub.addUser(newUserProto);
    }

    @Override
    public LoginResponseProto login(String username, String password, String code) {
        String nonNullCode = code == null ? "" : code;
        LoginProto loginProto = LoginProto.newBuilder().setUsername(username).setPassword(password).setCode(nonNullCode).build();
        return this.stub.login(loginProto);
    }

    @Override
    public VerifyAccountResponseProto verifyUserAccount(String verificationToken) {
        VerifyAccountProto verifyAccountProto = VerifyAccountProto.newBuilder().setVerificationToken(verificationToken).build();
        return this.stub.verifyUserAccount(verifyAccountProto);
    }

    @Override
    public ChangePasswordResponseProto changePassword(String userId, String oldPassword, String newPassword, String repeatedNewPassword) {
        ChangePasswordProto passwordProto = ChangePasswordProto.newBuilder()
                .setUserId(userId)
                .setOldPassword(oldPassword)
                .setNewPassword(newPassword)
                .setRepeatedNewPassword(repeatedNewPassword)
                .build();
        return this.stub.changePassword(passwordProto);
    }

    @Override
    public EventResponseProto getEvents() {
        EventProto eventProto = EventProto.newBuilder().build();
        return stub.getEvents(eventProto);
    }

    @Override
    public SendTokenResponseProto recoverAccount(String id, String email) {
        SendTokenProto sendTokenProto = SendTokenProto.newBuilder().setId(id).setEmail(email).build();
        return this.stub.recoverAccount(sendTokenProto);
    }

    @Override
    public RecoveryPasswordResponseProto changePasswordRecovery(String newPassword, String repeatedNewPassword, String token) {
        RecoveryPasswordProto recoveryPasswordProto = RecoveryPasswordProto.newBuilder()
                .setPassword(newPassword)
                .setRepeatedPassword(repeatedNewPassword)
                .setToken(token)
                .build();
        return this.stub.changePasswordRecovery(recoveryPasswordProto);
    }

    @Override
    public SendTokenResponseProto generateTokenPasswordless(String id, String email) {
        SendTokenProto sendTokenProto = SendTokenProto.newBuilder().setId(id).setEmail(email).build();
        return this.stub.generateTokenPasswordless(sendTokenProto);
    }

    @Override
    public LoginResponseProto passwordlessLogin(String verificationToken) {
        VerifyAccountProto verifyAccountProto = VerifyAccountProto.newBuilder().setVerificationToken(verificationToken).build();
        return this.stub.passwordlessLogin(verifyAccountProto);
    }

    @Override
    public LoginResponseProto refreshToken(String refreshToken) {
        RefreshTokenProto refreshTokenProto = RefreshTokenProto.newBuilder().setToken(refreshToken).build();
        return this.stub.refreshToken(refreshTokenProto);
    }

    @Override
    public SendTokenResponseProto checkToken(String checkToken) {
        TokenProto tokenProto = TokenProto.newBuilder().setToken(checkToken).build();
        return this.stub.checkToken(tokenProto);
    }

    @Override
    public APITokenResponseProto generateAPIToken(String userId) {
        APITokenProto apiTokenProto = APITokenProto.newBuilder().setUserId(userId).build();
        return this.stub.generateAPIToken(apiTokenProto);
    }

    @Override
    public Change2FAStatusResponseProto change2FAStatus(String userId, boolean enable2FA) {
        Change2FAStatusProto change2FAStatusProto = Change2FAStatusProto.newBuilder().setUserId(userId).setEnable2FA(enable2FA).build();
        return this.stub.change2FAStatus(change2FAStatusProto);
    }

    @Override
    public TwoFAStatusResponseProto checkTwoFaStatus(String userId) {
        TwoFAStatusProto twoFAStatusProto = TwoFAStatusProto.newBuilder().setUserId(userId).build();
        return this.stub.check2FAStatus(twoFAStatusProto);
    }
}
