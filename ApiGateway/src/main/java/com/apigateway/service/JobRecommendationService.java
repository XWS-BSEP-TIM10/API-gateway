package com.apigateway.service;

import com.apigateway.dto.CreateJobAdRequestDTO;
import com.apigateway.dto.NewInterestDTO;
import proto.RemoveInterestResponseProto;

public interface JobRecommendationService {
    RemoveInterestResponseProto add(NewInterestDTO dto);
    RemoveInterestResponseProto addJobAd(CreateJobAdRequestDTO dto, String userId);
}
