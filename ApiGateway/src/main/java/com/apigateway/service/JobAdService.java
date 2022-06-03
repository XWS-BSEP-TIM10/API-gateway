package com.apigateway.service;

import com.apigateway.dto.CreateJobAdRequestDTO;
import proto.JobAdResponseProto;

public interface JobAdService {
    JobAdResponseProto add(CreateJobAdRequestDTO createDto, String userId);
}
