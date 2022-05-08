package com.apigateway.controller;

import com.apigateway.dto.*;
import com.apigateway.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proto.LoginResponseProto;
import proto.NewUserResponseProto;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<NewUserResponseDTO> addUser(@RequestBody NewUserDTO newUserDTO) {
        NewUserResponseProto response = authService.signUp(newUserDTO);
        if(response.getStatus().equals("Status 400"))
            return ResponseEntity.badRequest().build();
        if(response.getStatus().equals("Status 500"))
            return ResponseEntity.internalServerError().build();
        NewUserResponseDTO newUserResponseDTO = new NewUserResponseDTO(response.getId(), newUserDTO);
        return new ResponseEntity<>(newUserResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        try {
            LoginResponseProto response = authService.login(loginDTO.getUsername(), loginDTO.getPassword());
            if(response.getStatus().equals("Status 400"))
                return ResponseEntity.badRequest().build();
            return ResponseEntity.ok(new TokenDTO(response.getJwt()));
        }catch(Exception ex){
            return ResponseEntity.badRequest().build();
        }
    }



}
