package com.apigateway.controller;

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

import com.apigateway.dto.ExperienceDTO;
import com.apigateway.dto.NewExperienceDTO;
import com.apigateway.service.ExperienceService;

import proto.NewExperienceResponseProto;
import proto.RemoveExperienceResponseProto;
import proto.UpdateExperienceResponseProto;

@RestController
@RequestMapping(value = "/api/v1/experiences")
public class ExperienceController {
	
	private final ExperienceService experienceService;
	
	@Autowired
	public ExperienceController(ExperienceService experienceService) {
		this.experienceService = experienceService;
	}
	
	 @PreAuthorize("hasAuthority('CRUD_EXPERIENCE_PERMISSION')")
	 @PostMapping
	    public ResponseEntity<ExperienceDTO> add(@RequestBody NewExperienceDTO dto) {
			if(dto.getToDate().isEmpty()) dto.setToDate("Present");
	        NewExperienceResponseProto response = experienceService.add(dto);
			if(response.getStatus().equals("Status 404"))
			    return ResponseEntity.notFound().build();
			else if(response.getStatus().equals("Status 400"))
				return ResponseEntity.badRequest().build();
			return ResponseEntity.ok(new ExperienceDTO(response));
	    }
	 
	 @PreAuthorize("hasAuthority('CRUD_EXPERIENCE_PERMISSION')")
	 @PutMapping("{id}")
	    public ResponseEntity<ExperienceDTO> update(@PathVariable Long id, @RequestBody NewExperienceDTO dto) {
		 	if(dto.getToDate().isEmpty()) dto.setToDate("Present");
	        UpdateExperienceResponseProto response = experienceService.update(id, dto);
			if (response.getStatus().equals("Status 404"))
			    return ResponseEntity.notFound().build();
			else if(response.getStatus().equals("Status 400"))
				return ResponseEntity.badRequest().build();
			return ResponseEntity.ok(new ExperienceDTO(response));
	    }
	 
	 @PreAuthorize("hasAuthority('CRUD_EXPERIENCE_PERMISSION')")
	 @DeleteMapping("{id}")
	    public ResponseEntity<HttpStatus> remove(@PathVariable Long id) {
		 	RemoveExperienceResponseProto response= experienceService.remove(id);
	        if(response.getStatus().equals("Status 404")) return ResponseEntity.notFound().build();
	        return ResponseEntity.ok().build();
	    }

}
