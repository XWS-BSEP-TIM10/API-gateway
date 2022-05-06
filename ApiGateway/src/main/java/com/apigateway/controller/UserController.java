package com.apigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apigateway.dto.NewUserDTO;
import com.apigateway.dto.UpdateResponseDTO;
import com.apigateway.service.UserService;

import proto.UpdateUserResponseProto;


@RestController
@RequestMapping(value = "/api/v1/profiles")
public class UserController {
	
private final UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	 @PutMapping
	    public ResponseEntity<UpdateResponseDTO> update(@RequestBody NewUserDTO dto) {
	        UpdateUserResponseProto response = userService.update(dto);
			if(response.getStatus().equals("Status 400")) return ResponseEntity.badRequest().build();
			return ResponseEntity.ok(new UpdateResponseDTO(response)); 
	    }

}
