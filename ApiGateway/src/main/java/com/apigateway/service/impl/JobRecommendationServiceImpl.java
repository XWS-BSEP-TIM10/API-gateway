package com.apigateway.service.impl;

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

}
