package com.apigateway.service;

import com.apigateway.dto.NewExperienceDTO;

import proto.NewExperienceResponseProto;
import proto.RemoveExperienceResponseProto;
import proto.UpdateExperienceResponseProto;

public interface ExperienceService {

	NewExperienceResponseProto add(NewExperienceDTO dto);
	
	UpdateExperienceResponseProto update(Long id, NewExperienceDTO dto);
	
	 RemoveExperienceResponseProto remove(Long id);
}
