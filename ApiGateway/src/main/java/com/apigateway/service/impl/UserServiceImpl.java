package com.apigateway.service.impl;

import org.springframework.stereotype.Component;

import com.apigateway.dto.NewUserDTO;
import com.apigateway.service.UserService;

import net.devh.boot.grpc.client.inject.GrpcClient;
import proto.FindUserProto;
import proto.FindUserResponseProto;
import proto.UpdateUserProto;
import proto.UpdateUserResponseProto;
import proto.UserGrpcServiceGrpc;

@Component
public class UserServiceImpl implements UserService{
	
	@GrpcClient("profilegrpcservice")
	private UserGrpcServiceGrpc.UserGrpcServiceBlockingStub stub;
	
	public UpdateUserResponseProto update(NewUserDTO dto) {
		UpdateUserProto updateUserProto = UpdateUserProto.newBuilder().setUuid(dto.getUuid()).setFirstName(dto.getFirstName()).setLastName(dto.getLastName()).setEmail(dto.getEmail()).setPhoneNumber(dto.getPhoneNumber()).setGender(dto.getGender()).setDateOfBirth(dto.getDateOfBirth()).setUsername(dto.getUsername()).setPassword(dto.getPassword()).setBiography(dto.getBiography()).build();
		return this.stub.update(updateUserProto);
		
	}
	
	public FindUserResponseProto find(String firstName, String lastName) {
		FindUserProto findUserProto = FindUserProto.newBuilder().setFirstName(firstName).setLastName(lastName).build();
		return this.stub.find(findUserProto);
	}

}
