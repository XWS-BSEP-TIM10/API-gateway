package com.apigateway.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.apigateway.dto.APITokenRequestDTO;
import com.apigateway.dto.APITokenResponseDTO;
import com.apigateway.dto.ChangePasswordDto;
import com.apigateway.dto.LoginDTO;
import com.apigateway.dto.NewUserDTO;
import com.apigateway.dto.NewUserResponseDTO;
import com.apigateway.dto.PasswordDto;
import com.apigateway.dto.TokenDTO;
import com.apigateway.dto.TwoFADTO;
import com.apigateway.dto.TwoFAResponseDTO;
import com.apigateway.dto.TwoFAStatusDTO;
import com.apigateway.service.AuthService;
import com.apigateway.service.LoggerService;
import com.apigateway.service.UserService;
import com.apigateway.service.impl.LoggerServiceImpl;

import io.grpc.StatusRuntimeException;
import io.micrometer.core.instrument.MeterRegistry;
import proto.APITokenResponseProto;
import proto.Change2FAStatusResponseProto;
import proto.ChangePasswordResponseProto;
import proto.LoginResponseProto;
import proto.NewUserResponseProto;
import proto.RecoveryPasswordResponseProto;
import proto.SendTokenResponseProto;
import proto.TwoFAStatusResponseProto;
import proto.VerifyAccountResponseProto;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final LoggerService loggerService;

    private static final String CONFLICT_STATUS = "Status 409";
    private static final String TEAPOT_STATUS = "Status 418";
    private static final String NOT_FOUND_STATUS = "Status 404";
    private static final String BAD_REQUEST_STATUS = "Status 400";
    private static final String TOKEN_EXPIRED = "Token expired";
    
    public static MeterRegistry registry;  
    
    public AuthController(AuthService authService, UserService userService, MeterRegistry registry) {
        this.authService = authService;
        this.userService = userService;
        this.loggerService = new LoggerServiceImpl(this.getClass());
        AuthController.registry = registry;
        
    }

    @PostMapping("/signup")
    public ResponseEntity<NewUserResponseDTO> addUser(@Valid @RequestBody NewUserDTO newUserDTO, HttpServletRequest request) {
        try {
            newUserDTO.setId(userService.getId(newUserDTO.getEmail()).getId());
            NewUserResponseProto response = authService.signUp(newUserDTO);
            if (response.getStatus().equals(BAD_REQUEST_STATUS)) {
                return ResponseEntity.badRequest().build();
            }
            if (response.getStatus().equals(CONFLICT_STATUS) || response.getStatus().equals(TEAPOT_STATUS)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            NewUserResponseDTO newUserResponseDTO = new NewUserResponseDTO(response.getId(), newUserDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
            	      .body(newUserResponseDTO);
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO loginDTO, HttpServletRequest request) {
        try {
            LoginResponseProto response = authService.login(loginDTO.getUsername(), loginDTO.getPassword(), loginDTO.getCode());
            if (response.getStatus().equals("Status 300")) {
                return ResponseEntity.status(300).build();
            }
            if (response.getStatus().equals(BAD_REQUEST_STATUS)) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(new TokenDTO(response.getJwt(), response.getRefreshToken()));
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PreAuthorize("hasAuthority('UPDATE_2FA_STATUS')")
    @PutMapping(value = "/2fa")
    public ResponseEntity<TwoFAResponseDTO> change2FAStatus(@RequestBody TwoFADTO twoFADTO) {
        Change2FAStatusResponseProto response = authService.change2FAStatus(twoFADTO.getUserId(), twoFADTO.isEnable2FA());
        if (response.getStatus().equals(NOT_FOUND_STATUS)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new TwoFAResponseDTO(response.getSecret()));
    }

    @PreAuthorize("hasAuthority('CHECK_2FA_STATUS')")
    @GetMapping(value = "/2fa/status/{userId}")
    public ResponseEntity<TwoFAStatusDTO> check2FAStatus(@PathVariable String userId, HttpServletRequest request) {
        TwoFAStatusResponseProto response = authService.checkTwoFaStatus(userId);
        if (response.getStatus().equals(NOT_FOUND_STATUS)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new TwoFAStatusDTO(response.getEnabled2FA()));
    }

    @GetMapping(value = "/confirm/{token}")
    public ResponseEntity<HttpStatus> confirmToken(@PathVariable String token, HttpServletRequest request) {
        try {     	
            VerifyAccountResponseProto response = authService.verifyUserAccount(token);
            if (response.getStatus().equals(BAD_REQUEST_STATUS)) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok().build();

        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PreAuthorize("hasAuthority('CHANGE_PASSWORD_PERMISSION')")
    @PutMapping(value = "/changePassword")
    public ResponseEntity<HttpStatus> login(@RequestBody ChangePasswordDto changePasswordDto, HttpServletRequest request) {
        try {
            ChangePasswordResponseProto response = authService.changePassword(changePasswordDto.getUserId(), changePasswordDto.getOldPassword(), changePasswordDto.getNewPassword(), changePasswordDto.getRepeatedNewPassword());
            if (response.getStatus().equals(BAD_REQUEST_STATUS)) {
                return ResponseEntity.badRequest().build();
            }
            if (response.getStatus().equals(TEAPOT_STATUS)) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok().build();
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/recover")
    public ResponseEntity<HttpStatus> recoverAccount(@Email String email, HttpServletRequest request) {
        try {      	
            String id = userService.getId(email).getId();
            if (!id.equals("")) {
                SendTokenResponseProto recoverProto = authService.recoverAccount(id, email);
                if (recoverProto.getStatus().equals("Status 200")) {         
                    return ResponseEntity.ok().build();
                }        
                return ResponseEntity.notFound().build();
            }      
            return ResponseEntity.notFound().build();
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }

    }

    @PutMapping(value = "/recover/changePassword/{token}")
    public ResponseEntity<String> changePasswordRecovery(@PathVariable String token, @RequestBody PasswordDto passwordDto, HttpServletRequest request) {
        try {	
            RecoveryPasswordResponseProto recoveryPasswordResponseProto = authService.changePasswordRecovery(passwordDto.getNewPassword(), passwordDto.getRepeatedNewPassword(), token);
            if (recoveryPasswordResponseProto.getStatus().equals(TEAPOT_STATUS)) {
                loggerService.changePasswordRecoverFailed(TOKEN_EXPIRED, request.getRemoteAddr());
                return ResponseEntity.badRequest().body(TOKEN_EXPIRED);
            }
            if (recoveryPasswordResponseProto.getStatus().equals("Status 400")) {
                loggerService.changePasswordRecoverFailed("Passwords not matching", request.getRemoteAddr());
                return ResponseEntity.badRequest().build();
            }
            loggerService.passwordRecovered(request.getRemoteAddr());
            return ResponseEntity.ok().build();
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/passwordless")
    public ResponseEntity<HttpStatus> passwordlessToken(String email, HttpServletRequest request) {
        try {
            String id = userService.getId(email).getId();
            if (!id.equals("")) {
                SendTokenResponseProto recoverProto = authService.generateTokenPasswordless(id, email);
                if (recoverProto.getStatus().equals("Status 200")) {
                    return ResponseEntity.ok().build();
                }
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.notFound().build();
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping(value = "/login/passwordless/{token}")
    public ResponseEntity<TokenDTO> passwordlessLogin(@PathVariable String token, HttpServletRequest request) {
        try {      	
            LoginResponseProto loginResponseProto = authService.passwordlessLogin(token);
            if (loginResponseProto.getStatus().equals(BAD_REQUEST_STATUS)) {
                loggerService.passwordlessLoginFailed(TOKEN_EXPIRED, request.getRemoteAddr());
                return ResponseEntity.badRequest().build();
            }
            loggerService.passwordlessLoginSuccess(request.getRemoteAddr());
            return ResponseEntity.ok(new TokenDTO(loginResponseProto.getJwt(), loginResponseProto.getRefreshToken()));
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/refreshToken")
    public ResponseEntity<TokenDTO> refreshToken(@RequestHeader("Authorization") String token, HttpServletRequest request) {
        try {	
            LoginResponseProto response = authService.refreshToken(token);
            return ResponseEntity.ok(new TokenDTO(response.getJwt(), response.getRefreshToken()));
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/checkToken/{token}")
    public ResponseEntity<HttpStatus> checkToken(@PathVariable String token, HttpServletRequest request) {
        try {  	
            SendTokenResponseProto response = authService.checkToken(token);
            if (response.getStatus().equals(NOT_FOUND_STATUS)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().build();
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }

    }

    @PreAuthorize("hasAuthority('CREATE_API_TOKEN')")
    @PostMapping("/api-token")
    public ResponseEntity<APITokenResponseDTO> generateAPIToken(@RequestBody APITokenRequestDTO requestDTO, HttpServletRequest request) {
        try {       	
            APITokenResponseProto response = authService.generateAPIToken(requestDTO.getUserId());
            loggerService.apiTokenGenerated(requestDTO.getUserId());
            return ResponseEntity.ok(new APITokenResponseDTO(response.getToken()));
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('GET_EVENTS_PERMISSION')")
    @GetMapping(value = "/events")
    public ResponseEntity<List<String>> getEvents() {
        List<String> events = new ArrayList<>();
        for(String event : authService.getEvents().getEventsList()){
            events.add(event);
        }
        return ResponseEntity.ok(events);
    }
}
