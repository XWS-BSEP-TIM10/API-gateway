package com.apigateway.service;

import com.apigateway.dto.CreateJobAdRequestDTO;
import com.apigateway.dto.NewExperienceDTO;
import com.apigateway.dto.NewInterestDTO;
import proto.*;

public interface JobRecommendationService {
    RemoveInterestResponseProto add(NewInterestDTO dto);
    RemoveInterestResponseProto addExperience(NewExperienceDTO dto);
    RemoveInterestResponseProto addJobAd(CreateJobAdRequestDTO dto, String userId);
    JobAdRecommendationsResponseProto findRecommendations(String userId);
    JobRecommendationEventResponseProto getEvents();
}
