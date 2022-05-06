package com.apigateway.service;

import com.apigateway.dto.NewInterestDTO;

import proto.NewInterestResponseProto;
import proto.RemoveInterestResponseProto;

public interface InterestService {
	
	NewInterestResponseProto add(NewInterestDTO dto);
	
	RemoveInterestResponseProto remove(Long id, String userId);

}
