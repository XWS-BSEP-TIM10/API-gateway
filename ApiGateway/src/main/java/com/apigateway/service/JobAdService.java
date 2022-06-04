package com.apigateway.service;

import com.apigateway.dto.CreateJobAdRequestDTO;
import proto.GetJobAdsResponseProto;
import proto.JobAdResponseProto;

public interface JobAdService {
    JobAdResponseProto add(CreateJobAdRequestDTO createDto, String userId);

    GetJobAdsResponseProto getUserJobAds(String userId);

    GetJobAdsResponseProto getJobAds(String search);
}
