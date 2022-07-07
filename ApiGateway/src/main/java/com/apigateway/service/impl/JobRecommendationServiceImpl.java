package com.apigateway.service.impl;

import com.apigateway.dto.CreateJobAdRequestDTO;
import com.apigateway.dto.NewExperienceDTO;
import com.apigateway.dto.NewInterestDTO;
import com.apigateway.service.JobRecommendationService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import proto.*;

@Component
public class JobRecommendationServiceImpl implements JobRecommendationService {

    @GrpcClient("jobrecommendationgrpcservice")
    private JobRecommendationGrpcServiceGrpc.JobRecommendationGrpcServiceBlockingStub stub;

    @Override
    public RemoveInterestResponseProto add(NewInterestDTO dto) {
        NewInterestProto requestProto = NewInterestProto.newBuilder().setUserId(dto.getUserId())
                                                                    .setDescription(dto.getDescription())
                                                                    .build();
        return this.stub.add(requestProto);
    }

    @Override
    public RemoveInterestResponseProto addExperience(NewExperienceDTO dto) {
        NewInterestProto requestProto = NewInterestProto.newBuilder().setUserId(dto.getUserId())
                .setDescription(dto.getPosition())
                .build();
        return this.stub.add(requestProto);
    }

    @Override
    public RemoveInterestResponseProto addJobAd(CreateJobAdRequestDTO dto, String userId) {
        JobAdRequestProto requestProto = JobAdRequestProto.newBuilder().setUserId(userId).setTitle(dto.getTitle())
                .setPosition(dto.getPosition()).setDescription(dto.getDescription())
                .setCompany(dto.getCompany()).addAllRequirements(dto.getRequirements()).build();
        return this.stub.addJob(requestProto);
    }

    @Override
    public JobAdRecommendationsResponseProto findRecommendations(String userId) {
        UserIdRequestProto requestProto = UserIdRequestProto.newBuilder().setUserId(userId).build();
        return this.stub.findRecommendations(requestProto);
    }

}
