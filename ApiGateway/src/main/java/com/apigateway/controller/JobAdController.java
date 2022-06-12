package com.apigateway.controller;

import com.apigateway.dto.CreateJobAdRequestDTO;
import com.apigateway.dto.CreateJobAdResponseDTO;
import com.apigateway.dto.JobAdDTO;
import com.apigateway.security.util.TokenUtils;
import com.apigateway.service.JobAdService;
import com.apigateway.service.impl.LoggerServiceImpl;
import io.grpc.StatusRuntimeException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import proto.GetJobAdsResponseProto;
import proto.JobAdResponseProto;
import proto.UserJobAdProto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class JobAdController {

    private final JobAdService jobAdService;

    private final TokenUtils tokenUtils;

    private final LoggerServiceImpl loggerService;

    public JobAdController(JobAdService jobAdService, TokenUtils tokenUtils) {
        this.jobAdService = jobAdService;
        this.tokenUtils = tokenUtils;
        this.loggerService = new LoggerServiceImpl(this.getClass());
    }

    @PreAuthorize("hasAuthority('CREATE_JOB_AD')")
    @PostMapping("job-ads")
    public ResponseEntity<CreateJobAdResponseDTO> test(@RequestHeader(value = "DislinktAuth", required = false) String agentToken,
                                                       @RequestHeader(value = "Authorization", required = false) String jwtToken,
                                                       @Valid @RequestBody CreateJobAdRequestDTO createDto, HttpServletRequest request) {
        try {
            String userId;
            if(agentToken != null) userId = tokenUtils.getUserIdFromToken(agentToken);
            else userId = tokenUtils.getUsernameFromToken(jwtToken.substring(7));
            JobAdResponseProto response = jobAdService.add(createDto, userId);
            if(response.getStatus().equals("Status 404")) return ResponseEntity.notFound().build();
            if(response.getStatus().equals("Status 500")) return ResponseEntity.internalServerError().build();
            return ResponseEntity.ok(new CreateJobAdResponseDTO(response));
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('GET_JOB_ADS')")
    @GetMapping("users/{userId}/job-ads")
    public ResponseEntity<List<JobAdDTO>> getUserJobAds(@PathVariable String userId, HttpServletRequest request) {
        try {
            GetJobAdsResponseProto response = jobAdService.getUserJobAds(userId);
            if(response.getStatus().equals("Status 404"))
                return ResponseEntity.notFound().build();
            List<JobAdDTO> jobAds = new ArrayList<>();
            for(UserJobAdProto jobAd : response.getJobAdsList()){
                JobAdDTO jobAdDto = new JobAdDTO(jobAd);
                jobAds.add(jobAdDto);
            }
            return ResponseEntity.ok(jobAds);
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('GET_JOB_ADS')")
    @GetMapping("job-ads")
    public ResponseEntity<List<JobAdDTO>> getJobAds(@RequestParam String search, HttpServletRequest request) {
        try {
            GetJobAdsResponseProto response = jobAdService.getJobAds(search);
            List<JobAdDTO> jobAds = new ArrayList<>();
            for (UserJobAdProto jobAd : response.getJobAdsList()) {
                JobAdDTO jobAdDto = new JobAdDTO(jobAd);
                jobAds.add(jobAdDto);
            }
            return ResponseEntity.ok(jobAds);
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }
}
