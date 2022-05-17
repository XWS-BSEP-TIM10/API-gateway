package com.apigateway.service.impl;

import org.springframework.stereotype.Component;

import com.apigateway.service.UserDetailsGrpcService;

import net.devh.boot.grpc.client.inject.GrpcClient;
import proto.UserDetailsGrpcServiceGrpc;
import proto.UserDetailsProto;
import proto.UserDetailsResponseProto;

@Component
public class UserDetailsGrpcServiceImpl implements UserDetailsGrpcService{
	
	@GrpcClient("authgrpcservice")
    private UserDetailsGrpcServiceGrpc.UserDetailsGrpcServiceBlockingStub stub;
	
		@Override
	    public UserDetailsResponseProto getUserDetails(String username) {
	        UserDetailsProto userDetailsProto = UserDetailsProto.newBuilder()
	                .setUsername(username)
	                .build();
	        return this.stub.getUserDetails(userDetailsProto);
	    }

}
