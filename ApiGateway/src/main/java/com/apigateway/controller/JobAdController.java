package com.apigateway.controller;

import com.apigateway.dto.CreateJobAdRequestDTO;
import com.apigateway.dto.CreateJobAdResponseDTO;
import com.apigateway.security.util.TokenUtils;
import com.apigateway.service.JobAdService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import proto.JobAdResponseProto;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/job-ads")
public class JobAdController {

    private final JobAdService jobAdService;

    private final TokenUtils tokenUtils;

    public JobAdController(JobAdService jobAdService, TokenUtils tokenUtils) {
        this.jobAdService = jobAdService;
        this.tokenUtils = tokenUtils;
    }

    @PreAuthorize("hasAuthority('CREATE_JOB_AD')")
    @PostMapping
    public ResponseEntity<CreateJobAdResponseDTO> test(@RequestHeader(value = "DislinktAuth", required = false) String agentToken,
                                           @RequestHeader(value = "Authorization", required = false) String jwtToken,
                                           @Valid @RequestBody CreateJobAdRequestDTO createDto) {
        String userId;
        if(agentToken != null) userId = tokenUtils.getUserIdFromToken(agentToken);
        else userId = tokenUtils.getUsernameFromToken(jwtToken.substring(7));
        JobAdResponseProto response = jobAdService.add(createDto, userId);
        if(response.getStatus().equals("Status 404")) return ResponseEntity.notFound().build();
        if(response.getStatus().equals("Status 500")) return ResponseEntity.internalServerError().build();
        return ResponseEntity.ok(new CreateJobAdResponseDTO(response));
    }
}
