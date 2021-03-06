package com.apigateway.service.impl;

import com.apigateway.dto.CreateJobAdRequestDTO;
import com.apigateway.service.JobAdService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import proto.*;

@Service
public class JobAdServiceImpl implements JobAdService {

    @GrpcClient("profilegrpcservice")
    private JobAdGrpcServiceGrpc.JobAdGrpcServiceBlockingStub stub;

    @Override
    public JobAdResponseProto add(CreateJobAdRequestDTO createDto, String userId) {
        JobAdProto newJobAd = JobAdProto.newBuilder().setUserId(userId).setTitle(createDto.getTitle())
                .setPosition(createDto.getPosition()).setDescription(createDto.getDescription())
                .setCompany(createDto.getCompany()).addAllRequirements(createDto.getRequirements()).build();

        return this.stub.addNewJobAd(newJobAd);
    }

    @Override
    public GetJobAdsResponseProto getUserJobAds(String userId) {
        GetJobAdsRequestProto request = GetJobAdsRequestProto.newBuilder().setUserId(userId).build();
        return this.stub.getUserJobAds(request);
    }

    @Override
    public GetJobAdsResponseProto getJobAds(String search) {
        SearchJobAdsProto request = SearchJobAdsProto.newBuilder().setSearchParam(search).build();
        return this.stub.getJobAds(request);
    }
}
