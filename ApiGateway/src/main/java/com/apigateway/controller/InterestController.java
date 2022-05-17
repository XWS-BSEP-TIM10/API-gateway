package com.apigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apigateway.dto.InterestDTO;
import com.apigateway.dto.NewInterestDTO;
import com.apigateway.service.InterestService;

import proto.NewInterestResponseProto;
import proto.RemoveInterestResponseProto;

@RestController
@RequestMapping(value = "/api/v1/interests")
public class InterestController {
	
private final InterestService interestService;
	
	@Autowired
	public InterestController(InterestService interestService) {
		this.interestService = interestService;
	}
	
	@PreAuthorize("hasAuthority('CRUD_INTEREST_PERMISSION')")
	@PostMapping
    public ResponseEntity<InterestDTO> add(@RequestBody NewInterestDTO dto) {
		NewInterestResponseProto response = interestService.add(dto);
        if(response.getStatus().equals("Status 404"))
            return  ResponseEntity.notFound().build();
        return ResponseEntity.ok(new InterestDTO(response));
    }
	
	 @PreAuthorize("hasAuthority('CRUD_INTEREST_PERMISSION')")
	 @DeleteMapping("{id}/user/{userId}")
	    public ResponseEntity<HttpStatus> remove(@PathVariable Long id, @PathVariable String userId) {
		 	RemoveInterestResponseProto response = interestService.remove(id, userId);
		 	if(response.getStatus().equals("Status 404")) return ResponseEntity.notFound().build();
	        return ResponseEntity.ok().build();
	    }

}
