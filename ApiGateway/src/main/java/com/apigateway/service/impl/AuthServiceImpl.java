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
    public LoginResponseProto login(String username, String password) {
        LoginProto loginProto = LoginProto.newBuilder().setUsername(username)
                .setPassword(password).build();
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
    public RecoverResponseProto recoverAccount(String id, String email) {
        RecoverProto recoverProto = RecoverProto.newBuilder().setId(id).setEmail(email).build();
        return this.stub.recoverAccount(recoverProto);
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

}
