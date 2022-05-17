package com.apigateway.service.impl;

import com.apigateway.service.UserDetailsService;

import net.devh.boot.grpc.client.inject.GrpcClient;
import proto.UserDetailsGrpcServiceGrpc;
import proto.UserDetailsProto;
import proto.UserDetailsResponseProto;

public class UserDetailsServiceImpl implements UserDetailsService{
	
	@GrpcClient("authgrpcservice")
    private UserDetailsGrpcServiceGrpc.UserDetailsGrpcServiceBlockingStub stub;
	
	
	    public UserDetailsResponseProto getUserDetails(String username) {
	        UserDetailsProto userDetailsProto = UserDetailsProto.newBuilder()
	                .setUsername(username)
	                .build();
	        return this.stub.getUserDetails(userDetailsProto);
	    }

}
