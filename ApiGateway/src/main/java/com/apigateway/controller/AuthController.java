package com.apigateway.controller;

import com.apigateway.dto.APITokenRequestDTO;
import com.apigateway.dto.APITokenResponseDTO;
import com.apigateway.dto.ChangePasswordDto;
import com.apigateway.dto.LoginDTO;
import com.apigateway.dto.NewUserDTO;
import com.apigateway.dto.NewUserResponseDTO;
import com.apigateway.dto.PasswordDto;
import com.apigateway.dto.TokenDTO;
import com.apigateway.service.AuthService;
import com.apigateway.service.LoggerService;
import com.apigateway.service.UserService;
import com.apigateway.service.impl.LoggerServiceImpl;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
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
import proto.APITokenResponseProto;
import proto.ChangePasswordResponseProto;
import proto.LoginResponseProto;
import proto.NewUserResponseProto;
import proto.RecoveryPasswordResponseProto;
import proto.SendTokenResponseProto;
import proto.VerifyAccountResponseProto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final LoggerService loggerService;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
        this.loggerService = new LoggerServiceImpl(this.getClass());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> addUser(@Valid @RequestBody NewUserDTO newUserDTO, HttpServletRequest request) {
        try {
            newUserDTO.setId(userService.getId(newUserDTO.getEmail()).getId());
            NewUserResponseProto response = authService.signUp(newUserDTO);
            if (response.getStatus().equals("Status 400"))
                return ResponseEntity.badRequest().build();
            if (response.getStatus().equals("Status 409"))
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            if (response.getStatus().equals("Status 418"))
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            if (response.getStatus().equals("Status 500"))
                return ResponseEntity.internalServerError().build();
            NewUserResponseDTO newUserResponseDTO = new NewUserResponseDTO(response.getId(), newUserDTO);
            return new ResponseEntity<>(newUserResponseDTO, HttpStatus.CREATED);
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
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO loginDTO, HttpServletRequest request) {
        try {
            LoginResponseProto response = authService.login(loginDTO.getUsername(), loginDTO.getPassword());
            if (response.getStatus().equals("Status 400"))
                return ResponseEntity.badRequest().build();
            return ResponseEntity.ok(new TokenDTO(response.getJwt(), response.getRefreshToken()));
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/confirm/{token}")
    public ResponseEntity<?> confirmToken(@PathVariable String token, HttpServletRequest request) {
        try {
            VerifyAccountResponseProto response = authService.verifyUserAccount(token);
            if (response.getStatus().equals("Status 400"))
                return ResponseEntity.badRequest().build();
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
    public ResponseEntity<?> login(@RequestBody ChangePasswordDto changePasswordDto, HttpServletRequest request) {
        try {
            ChangePasswordResponseProto response = authService.changePassword(changePasswordDto.getUserId(), changePasswordDto.getOldPassword(), changePasswordDto.getNewPassword(), changePasswordDto.getRepeatedNewPassword());
            if (response.getStatus().equals("Status 400"))
                return ResponseEntity.badRequest().body(response.getMessage());
            if (response.getStatus().equals("Status 418"))
                return ResponseEntity.badRequest().body(response.getMessage());
            return ResponseEntity.ok().build();
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/recover")
    public ResponseEntity<?> recoverAccount(@Email String email, HttpServletRequest request) {
        try {
            String id = userService.getId(email).getId();
            if (!id.equals("")) {
                SendTokenResponseProto recoverProto = authService.recoverAccount(id, email);
                if (recoverProto.getStatus().equals("Status 200"))
                    return ResponseEntity.ok().build();
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.notFound().build();
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }

    }

    @PutMapping(value = "/recover/changePassword/{token}")
    public ResponseEntity<?> changePasswordRecovery(@PathVariable String token, @RequestBody PasswordDto passwordDto, HttpServletRequest request) {
        try {

            RecoveryPasswordResponseProto recoveryPasswordResponseProto = authService.changePasswordRecovery(passwordDto.getNewPassword(), passwordDto.getRepeatedNewPassword(), token);
            if (recoveryPasswordResponseProto.getStatus().equals("Status 418")) {
                loggerService.changePasswordRecoverFailed("Token expired", request.getUserPrincipal().getName(), request.getRemoteAddr());
                return ResponseEntity.badRequest().body("Token expired");
            }
            if (recoveryPasswordResponseProto.getStatus().equals("Status 400")) {
                loggerService.changePasswordRecoverFailed("Passwords not matching", request.getUserPrincipal().getName(), request.getRemoteAddr());
                return ResponseEntity.badRequest().body("Passwords not matching");
            }
            loggerService.passwordRecovered(request.getUserPrincipal().getName(), request.getRemoteAddr());
            return ResponseEntity.ok().build();
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/passwordless")
    public ResponseEntity<?> passwordlessToken(String email, HttpServletRequest request) {
        try {
            String id = userService.getId(email).getId();
            if (!id.equals("")) {
                SendTokenResponseProto recoverProto = authService.generateTokenPasswordless(id, email);
                if (recoverProto.getStatus().equals("Status 200"))
                    return ResponseEntity.ok().build();
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.notFound().build();
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping(value = "/login/passwordless/{token}")
    public ResponseEntity<?> passwordlessLogin(@PathVariable String token, HttpServletRequest request) {
        try {
            LoginResponseProto loginResponseProto = authService.passwordlessLogin(token);
            if (loginResponseProto.getStatus().equals("Status 400")) {
                loggerService.passwordlessLoginFailed("Token expired", request.getUserPrincipal().getName(), request.getRemoteAddr());
                return ResponseEntity.badRequest().body("Token expired");
            }
            loggerService.passwordlessLoginSuccess(request.getUserPrincipal().getName(), request.getRemoteAddr());
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
    public ResponseEntity<?> checkToken(@PathVariable String token, HttpServletRequest request) {
        try {
            SendTokenResponseProto response = authService.checkToken(token);
            if (response.getStatus().equals("Status 404")) {
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
            loggerService.APITokenGenerated(requestDTO.getUserId());
            return ResponseEntity.ok(new APITokenResponseDTO(response.getToken()));
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            return ResponseEntity.internalServerError().build();
        }
    }
}
