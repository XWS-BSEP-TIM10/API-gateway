package com.apigateway.service;

import proto.UserDetailsResponseProto;

public interface UserDetailsGrpcService {
	
	 UserDetailsResponseProto getUserDetails(String username);
}
