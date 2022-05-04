package com.apigateway.service.impl;

import com.apigateway.dto.NewUserDTO;
import com.apigateway.service.AuthService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import proto.AuthGrpcServiceGrpc;
import proto.NewUserProto;
import proto.NewUserResponseProto;

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
}
