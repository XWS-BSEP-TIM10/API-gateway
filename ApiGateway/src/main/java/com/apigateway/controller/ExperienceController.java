package com.apigateway.controller;

import com.apigateway.dto.ExperienceDTO;
import com.apigateway.dto.NewExperienceDTO;
import com.apigateway.service.ExperienceService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proto.NewExperienceResponseProto;
import proto.RemoveExperienceResponseProto;
import proto.UpdateExperienceResponseProto;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/v1/experiences")
public class ExperienceController {

    private final ExperienceService experienceService;
    private final LoggerService loggerService;
    private static final String NOT_FOUND_STATUS = "Status 404";

    @Autowired
    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
        this.loggerService = new LoggerServiceImpl(this.getClass());
    }

    @PreAuthorize("hasAuthority('CREATE_EXPERIENCE_PERMISSION')")
    @PostMapping
    public ResponseEntity<ExperienceDTO> add(@RequestBody NewExperienceDTO dto, HttpServletRequest request) {
        try {
            NewExperienceResponseProto response = experienceService.add(dto);
            if (response.getStatus().equals(NOT_FOUND_STATUS))
                return ResponseEntity.notFound().build();
            else if (response.getStatus().equals("Status 400"))
                return ResponseEntity.badRequest().build();
            return ResponseEntity.ok(new ExperienceDTO(response));
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('UPDATE_EXPERIENCE_PERMISSION')")
    @PutMapping("{id}")
    public ResponseEntity<ExperienceDTO> update(@PathVariable Long id, @RequestBody NewExperienceDTO dto, HttpServletRequest request) {
        try {
            UpdateExperienceResponseProto response = experienceService.update(id, dto);
            if (response.getStatus().equals(NOT_FOUND_STATUS))
                return ResponseEntity.notFound().build();
            else if (response.getStatus().equals("Status 400"))
                return ResponseEntity.badRequest().build();
            return ResponseEntity.ok(new ExperienceDTO(response));
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('DELETE_EXPERIENCE_PERMISSION')")
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> remove(@PathVariable Long id, HttpServletRequest request) {
        try {
            RemoveExperienceResponseProto response = experienceService.remove(id);
            if (response.getStatus().equals(NOT_FOUND_STATUS)) return ResponseEntity.notFound().build();
            return ResponseEntity.ok().build();
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

}
