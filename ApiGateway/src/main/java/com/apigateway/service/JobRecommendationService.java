package com.apigateway.service;

import com.apigateway.dto.CreateJobAdRequestDTO;
import com.apigateway.dto.NewInterestDTO;
import proto.JobAdRecommendationsResponseProto;
import proto.RemoveInterestResponseProto;
import proto.UserIdRequestProto;

public interface JobRecommendationService {
    RemoveInterestResponseProto add(NewInterestDTO dto);
    RemoveInterestResponseProto addJobAd(CreateJobAdRequestDTO dto, String userId);
    JobAdRecommendationsResponseProto findRecommendations(String userId);
}
