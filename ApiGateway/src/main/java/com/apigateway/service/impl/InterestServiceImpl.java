package com.apigateway.service.impl;

import org.springframework.stereotype.Component;

import com.apigateway.dto.NewInterestDTO;
import com.apigateway.service.InterestService;

import net.devh.boot.grpc.client.inject.GrpcClient;
import proto.InterestGrpcServiceGrpc;
import proto.NewInterestProto;
import proto.NewInterestResponseProto;
import proto.RemoveInterestProto;
import proto.RemoveInterestResponseProto;

@Component
public class InterestServiceImpl implements InterestService{
	
	@GrpcClient("profilegrpcservice")
	private InterestGrpcServiceGrpc.InterestGrpcServiceBlockingStub stub;
	
	public NewInterestResponseProto add(NewInterestDTO dto) {
		NewInterestProto newInterestProto = NewInterestProto.newBuilder().setUserId(dto.getUserId()).setDescription(dto.getDescription()).build();
		return this.stub.add(newInterestProto);
	}
	
	public RemoveInterestResponseProto remove(Long id, String userId) {
		RemoveInterestProto removeInterestProto = RemoveInterestProto.newBuilder().setId(id).setUserId(userId).build();
		return this.stub.remove(removeInterestProto);
	}

}
