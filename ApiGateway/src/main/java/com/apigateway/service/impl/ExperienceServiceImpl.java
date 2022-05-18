package com.apigateway.service.impl;

import org.springframework.stereotype.Component;

import com.apigateway.dto.NewExperienceDTO;
import com.apigateway.service.ExperienceService;


import net.devh.boot.grpc.client.inject.GrpcClient;
import proto.ExperienceGrpcServiceGrpc;
import proto.NewExperienceProto;
import proto.NewExperienceResponseProto;
import proto.RemoveExperienceProto;
import proto.RemoveExperienceResponseProto;
import proto.UpdateExperienceProto;
import proto.UpdateExperienceResponseProto;

@Component
public class ExperienceServiceImpl implements ExperienceService{
	
	@GrpcClient("profilegrpcservice")
	private ExperienceGrpcServiceGrpc.ExperienceGrpcServiceBlockingStub stub;

	@Override
	public NewExperienceResponseProto add(NewExperienceDTO dto) {
			NewExperienceProto newExperienceProto =NewExperienceProto.newBuilder().setUserId(dto.getUserId()).setInstitution(dto.getInstitution()).setPosition(dto.getPosition()).setFromDate(dto.getFromDate()).setToDate(dto.getToDate()).setDescription(dto.getDescription()).setType(dto.getType()).build();
			return this.stub.add(newExperienceProto);
	}

	@Override
	public UpdateExperienceResponseProto update(Long id, NewExperienceDTO dto) {
		UpdateExperienceProto updateExperienceProto =UpdateExperienceProto.newBuilder().setUserId(dto.getUserId()).setInstitution(dto.getInstitution()).setPosition(dto.getPosition()).setFromDate(dto.getFromDate()).setToDate(dto.getToDate()).setDescription(dto.getDescription()).setType(dto.getType()).setId(id).build();
		return this.stub.update(updateExperienceProto);
	}

	@Override
	public RemoveExperienceResponseProto remove(Long id) {
		RemoveExperienceProto removeExperienceProto =RemoveExperienceProto.newBuilder().setId(id).build();
		return this.stub.remove(removeExperienceProto);
	}
	
	


}
