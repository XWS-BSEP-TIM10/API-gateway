package com.apigateway.controller;

import com.apigateway.dto.InterestDTO;
import com.apigateway.dto.NewInterestDTO;
import com.apigateway.service.InterestService;
import com.apigateway.service.JobRecommendationService;
import com.apigateway.service.LoggerService;
import com.apigateway.service.impl.LoggerServiceImpl;
import io.grpc.StatusRuntimeException;
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
import proto.NewInterestResponseProto;
import proto.RemoveInterestResponseProto;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/v1/interests")
public class InterestController {

    private final InterestService interestService;

    private final LoggerService loggerService;

    private final JobRecommendationService jobRecommendationService;

    @Autowired
    public InterestController(InterestService interestService, JobRecommendationService jobRecommendationService) {
        this.interestService = interestService;
        this.jobRecommendationService = jobRecommendationService;
        this.loggerService = new LoggerServiceImpl(this.getClass());
    }

    @PreAuthorize("hasAuthority('CREATE_INTEREST_PERMISSION')")
    @PostMapping
    public ResponseEntity<InterestDTO> add(@RequestBody NewInterestDTO dto, HttpServletRequest request) {
        try {
            NewInterestResponseProto response = interestService.add(dto);
            RemoveInterestResponseProto jobRecommendationResponse = jobRecommendationService.add(dto);
            if (response.getStatus().equals("Status 404") || jobRecommendationResponse.getStatus().equals("Status 500"))
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(new InterestDTO(response));
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('DELETE_INTEREST_PERMISSION')")
    @DeleteMapping("{id}/user/{userId}")
    public ResponseEntity<HttpStatus> remove(@PathVariable Long id, @PathVariable String userId, HttpServletRequest request) {
        try {
            RemoveInterestResponseProto response = interestService.remove(id, userId);
            if (response.getStatus().equals("Status 404")) return ResponseEntity.notFound().build();
            return ResponseEntity.ok().build();
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

}
