package com.apigateway.controller;

import com.apigateway.dto.*;
import com.apigateway.model.User;
import com.apigateway.service.AuthService;
import com.apigateway.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import proto.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
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

    @GetMapping(value = "/confirm/{token}")
    public ResponseEntity<?> login(@PathVariable String token) {

        try {
            VerifyAccountResponseProto response = authService.verifyUserAccount(token);
            if(response.getStatus().equals("Status 400"))
                return ResponseEntity.badRequest().build();
            return ResponseEntity.ok("Account with username:" + response.getUsername() + " successfully activated!");
        }catch(Exception ex){
            return ResponseEntity.badRequest().build();
        }

    }

    @PreAuthorize("hasAuthority('CHANGE_PASSWORD_PERMISSION')")
    @PutMapping(value = "/changePassword")
    public ResponseEntity<?> login(@RequestBody ChangePasswordDto changePasswordDto) {
        try {
            ChangePasswordResponseProto response = authService.changePassword(changePasswordDto.getUserId(), changePasswordDto.getOldPassword(), changePasswordDto.getNewPassword(), changePasswordDto.getRepeatedNewPassword());
            if(response.getStatus().equals("Status 400"))
                return ResponseEntity.badRequest().body(response.getMessage());
            if(response.getStatus().equals("Status 418"))
                return ResponseEntity.badRequest().body(response.getMessage());
            return ResponseEntity.ok().build();
        }catch(Exception ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/recover")
    public ResponseEntity<?> recoverAccount(String email) {

        String id = userService.getId(email).getId();
        if (id != null) {
            SendTokenResponseProto recoverProto = authService.recoverAccount(id, email);
            if(recoverProto.getStatus().equals("Status 200"))
                return ResponseEntity.ok().build();
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();

    }

    @PutMapping(value = "/recover/changePassword/{token}")
    public ResponseEntity<?> changePasswordRecovery(@PathVariable String token, @RequestBody PasswordDto passwordDto) {


        RecoveryPasswordResponseProto recoveryPasswordResponseProto = authService.changePasswordRecovery(passwordDto.getNewPassword(), passwordDto.getRepeatedNewPassword(), token);
        if(recoveryPasswordResponseProto.getStatus().equals("Status 418")){
            return ResponseEntity.badRequest().body("Token expired");
        }else if(recoveryPasswordResponseProto.getStatus().equals("Status 400")){
            return ResponseEntity.badRequest().body("Passwords not matching");
        }else{
            return ResponseEntity.ok().build();
        }
    }
    
    @GetMapping(value = "/passwordless")
    public ResponseEntity<?> passwordlessToken(String email) {

        String id = userService.getId(email).getId();
        if (id != null) {
            SendTokenResponseProto recoverProto = authService.generateTokenPasswordless(id, email);
            if(recoverProto.getStatus().equals("Status 200"))
                return ResponseEntity.ok().build();
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();

    }
    
    @GetMapping(value = "/login/passwordless/{token}")
    public ResponseEntity<?> passwordlessLogin(@PathVariable String token) {

    	LoginResponseProto loginResponseProto = authService.passwordlessLogin(token);
        if(loginResponseProto.getStatus().equals("Status 400")){
            return ResponseEntity.badRequest().body("Token expired");
        }
        return ResponseEntity.ok(new TokenDTO(loginResponseProto.getJwt()));

    }

}
