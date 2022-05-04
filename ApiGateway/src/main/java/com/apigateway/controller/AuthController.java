package com.apigateway.controller;

import com.apigateway.dto.NewUserDTO;
import com.apigateway.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proto.NewUserResponseProto;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<NewUserDTO> addUser(@RequestBody NewUserDTO newUserDTO) {
        NewUserResponseProto newUserResponseProto = authService.signUp(newUserDTO);
        if(newUserResponseProto.getStatus().equals("Status 400"))
            return ResponseEntity.badRequest().build();
        return new ResponseEntity<>(newUserDTO, HttpStatus.CREATED);
    }


}
