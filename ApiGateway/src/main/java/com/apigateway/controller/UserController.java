package com.apigateway.controller;

import java.util.ArrayList;
import java.util.List;

import com.apigateway.service.ConnectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.apigateway.dto.UpdateUserDTO;
import com.apigateway.dto.UserDto;
import com.apigateway.service.UserService;

import proto.FindUserResponseProto;
import proto.UpdateUserResponseProto;
import proto.UserProto;
import proto.UserResponseProto;


@RestController
@RequestMapping(value = "/api/v1/profiles")
public class UserController {
	
	private final UserService userService;

	private final ConnectionsService connectionsService;
	
	@Autowired
	public UserController(UserService userService, ConnectionsService connectionsService) {
		this.userService = userService;
		this.connectionsService = connectionsService;
	}
	 
	 @PreAuthorize("hasAuthority('UPDATE_PROFILE_PERMISSION')")
	 @PutMapping
	 public ResponseEntity<UpdateUserDTO> update(@RequestBody UpdateUserDTO dto) {
	 	UpdateUserResponseProto response = userService.update(dto);
		if(response.getStatus().equals("Status 400")) return ResponseEntity.badRequest().build();
		if(response.getStatus().equals("Status 404")) return ResponseEntity.notFound().build();
		if(response.getStatus().equals("Status 409")) return ResponseEntity.status(HttpStatus.CONFLICT).build();
		return ResponseEntity.ok(dto);
	}
	 

	 @GetMapping("find")
	 public ResponseEntity<List<UserDto>> find(String first_name, String last_name) {
		FindUserResponseProto response = userService.find(first_name, last_name);
		List<UserDto> users = new ArrayList<>();
		for(UserProto userProto: response.getUsersList()) {
			UserDto dto = new UserDto(userProto);
		 	users.add(dto);
		}
		return ResponseEntity.ok(users);
	}

	@GetMapping("{id}")
	public ResponseEntity<UserDto> getProfile(@PathVariable String id, @RequestHeader("Authorization") String token) {
		String loggedUserId = userService.getIdByToken(token.split(" ")[1]);

		if(!loggedUserId.equals(id)){
			String connectionStatus = connectionsService.getConnectionStatus(loggedUserId, id).getConnectionStatus();
			if(!connectionStatus.equals("CONNECTED"))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		UserResponseProto response = userService.getById(id);
		if(response.getStatus().equals("Status 404"))
			return ResponseEntity.notFound().build();

		UserDto dto = new UserDto(response.getUser());
		return ResponseEntity.ok(dto);
	}

	@PreAuthorize("hasAuthority('ADMIN_PERMISSION')")
	@GetMapping
	public ResponseEntity<?> admin() {
		return ResponseEntity.ok().build();
	}
}
