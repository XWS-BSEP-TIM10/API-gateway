package com.apigateway.controller;

import com.apigateway.dto.CreateJobAdRequestDTO;
import com.apigateway.dto.CreateJobAdResponseDTO;
import com.apigateway.dto.JobAdDTO;
import com.apigateway.security.util.TokenUtils;
import com.apigateway.service.ConnectionsService;
import com.apigateway.service.JobAdService;
import com.apigateway.service.JobRecommendationService;
import com.apigateway.service.LoggerService;
import com.apigateway.service.UserService;
import com.apigateway.service.impl.LoggerServiceImpl;
import io.grpc.StatusRuntimeException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import proto.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class JobAdController {

    private final JobAdService jobAdService;

    private final TokenUtils tokenUtils;

    private final LoggerService loggerService;

    private final JobRecommendationService jobRecommendationService;

    private final UserService userService;

    private final ConnectionsService connectionsService;

    public JobAdController(JobAdService jobAdService, TokenUtils tokenUtils, JobRecommendationService jobRecommendationService, UserService userService, ConnectionsService connectionsService) {
        this.jobAdService = jobAdService;
        this.tokenUtils = tokenUtils;
        this.jobRecommendationService = jobRecommendationService;
        this.userService = userService;
        this.connectionsService = connectionsService;
        this.loggerService = new LoggerServiceImpl(this.getClass());
    }

    @PreAuthorize("hasAuthority('CREATE_JOB_AD')")
    @PostMapping("job-ads")
    public ResponseEntity<CreateJobAdResponseDTO> test(@RequestHeader(value = "DislinktAuth", required = false) String agentToken,
                                                       @RequestHeader(value = "Authorization", required = false) String jwtToken,
                                                       @Valid @RequestBody CreateJobAdRequestDTO createDto, HttpServletRequest request) {
        try {
            String userId;
            if (agentToken != null) userId = tokenUtils.getUserIdFromToken(agentToken);
            else userId = tokenUtils.getUsernameFromToken(jwtToken.substring(7));
            JobAdResponseProto response = jobAdService.add(createDto, userId);
            RemoveInterestResponseProto recommendationResponse = jobRecommendationService.addJobAd(createDto, userId);
            if (response.getStatus().equals("Status 404")) return ResponseEntity.notFound().build();
            if (response.getStatus().equals("Status 500") || recommendationResponse.getStatus().equals("Status 500")) return ResponseEntity.internalServerError().build();
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
            if (response.getStatus().equals("Status 404"))
                return ResponseEntity.notFound().build();
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

    @PreAuthorize("hasAuthority('GET_JOB_ADS')")
    @GetMapping("job-ads/recommendations/{userId}")
    public ResponseEntity<List<JobAdDTO>> getJobAds(@PathVariable String userId) {
        try {
            JobAdRecommendationsResponseProto response = jobRecommendationService.findRecommendations(userId);
            List<JobAdDTO> jobAds = new ArrayList<>();
            for (JobAdRequestProto jobAd : response.getRecommendationsList()) {
                UserNamesResponseProto userNamesResponseProto = userService.getFirstAndLastName(jobAd.getUserId());
                JobAdDTO jobAdDto = new JobAdDTO(jobAd, userNamesResponseProto.getFirstName(), userNamesResponseProto.getLastName());
                jobAds.add(jobAdDto);
            }
            return ResponseEntity.ok(jobAds);
        } catch (StatusRuntimeException ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('GET_EVENTS_PERMISSION')")
    @GetMapping(value = "/job_recommendation/events")
    public ResponseEntity<List<String>> getEvents() {
        List<String> events = new ArrayList<>();
        for(String event : jobRecommendationService.getEvents().getEventsList()){
            events.add(event);
        }
        return ResponseEntity.ok(events);
    }
}
