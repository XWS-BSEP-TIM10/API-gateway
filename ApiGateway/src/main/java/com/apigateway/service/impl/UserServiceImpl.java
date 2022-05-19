package com.apigateway.service.impl;

import org.springframework.stereotype.Component;

import com.apigateway.dto.UpdateUserDTO;
import com.apigateway.service.UserService;

import net.devh.boot.grpc.client.inject.GrpcClient;
import proto.*;

@Component
public class UserServiceImpl implements UserService{
	
	@GrpcClient("profilegrpcservice")
	private UserGrpcServiceGrpc.UserGrpcServiceBlockingStub stub;

	@Override
	public UpdateUserResponseProto update(UpdateUserDTO dto) {
		UpdateUserProto updateUserProto = UpdateUserProto.newBuilder().setUuid(dto.getUuid()).setFirstName(dto.getFirstName()).setLastName(dto.getLastName()).setEmail(dto.getEmail()).setPhoneNumber(dto.getPhoneNumber()).setGender(dto.getGender()).setDateOfBirth(dto.getDateOfBirth()).setUsername(dto.getUsername()).setBiography(dto.getBiography()).build();
		return this.stub.update(updateUserProto);
		
	}

	@Override
	public FindUserResponseProto find(String firstName, String lastName) {
		FindUserProto findUserProto = FindUserProto.newBuilder().setFirstName(firstName).setLastName(lastName).build();
		return this.stub.find(findUserProto);
	}

	@Override
	public UserNamesResponseProto getFirstAndLastName(String id){
		UserNamesProto userNamesResponseProto = UserNamesProto.newBuilder().setId(id).build();
		return this.stub.getFirstAndLastName(userNamesResponseProto);
	}

	@Override
	public EmailResponseProto getEmail(String id) {
		EmailProto emailProto = EmailProto.newBuilder().setId(id).build();
		return this.stub.getEmail(emailProto);
	}

	@Override
	public IdResponseProto getId(String email) {
		IdProto idProto = IdProto.newBuilder().setEmail(email).build();
		return this.stub.getId(idProto);
	}
}
