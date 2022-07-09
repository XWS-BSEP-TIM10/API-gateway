package com.apigateway.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apigateway.dto.UpdateUserDTO;
import com.apigateway.dto.UserDto;
import com.apigateway.service.LoggerService;
import com.apigateway.service.UserService;
import com.apigateway.service.impl.LoggerServiceImpl;

import io.grpc.StatusRuntimeException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import proto.FindUserResponseProto;
import proto.UpdateUserResponseProto;
import proto.UserProto;
import proto.UserResponseProto;


@RestController
@RequestMapping(value = "/api/v1/profiles")
public class UserController {

    private final UserService userService;

    private final LoggerService loggerService;
    private final Counter httpRequests;
    
    private static final String HTTP_STATUS_TAG = "http_status";
    private static final String COUNTER_NAME = "http_requests";

    @Autowired
    public UserController(UserService userService, MeterRegistry registry) {
        this.userService = userService;
        this.loggerService = new LoggerServiceImpl(this.getClass());
        this.httpRequests = Counter.builder(COUNTER_NAME)
                .description("Number of HTTP requests for server endpoints")
                .tag(HTTP_STATUS_TAG, "200")
                .register(registry);
    }

    @PreAuthorize("hasAuthority('UPDATE_PROFILE_PERMISSION')")
    @PutMapping
    public ResponseEntity<UpdateUserDTO> update(@RequestBody UpdateUserDTO dto, HttpServletRequest request) {
        try {
            UpdateUserResponseProto response = userService.update(dto);
            if (response.getStatus().equals("Status 400")) {
            	Metrics.counter("http_requests", HTTP_STATUS_TAG, "400").increment();
            	return ResponseEntity.badRequest().build();
            }
            if (response.getStatus().equals("Status 404")) {
            	Metrics.counter("http_requests", HTTP_STATUS_TAG, "404").increment();
            	return ResponseEntity.notFound().build();
            }
            if (response.getStatus().equals("Status 409")) {
            	Metrics.counter("http_requests", HTTP_STATUS_TAG, "409").increment();
            	return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.ok(dto);
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            Metrics.counter("http_requests", HTTP_STATUS_TAG, "500").increment();
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("find")
    public ResponseEntity<List<UserDto>> find(String first_name, String last_name, HttpServletRequest request) {
        try {
            FindUserResponseProto response = userService.find(first_name, last_name);
            List<UserDto> users = new ArrayList<>();
            for (UserProto userProto : response.getUsersList()) {
                UserDto dto = new UserDto(userProto);
                users.add(dto);
            }
            Metrics.counter("http_requests", HTTP_STATUS_TAG, "200").increment();
            return ResponseEntity.ok(users);
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            Metrics.counter("http_requests", HTTP_STATUS_TAG, "500").increment();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getProfile(@PathVariable String id, @RequestHeader("Authorization") String token, HttpServletRequest request) {
        try {
            UserResponseProto response = userService.getById(id);
            if (response.getStatus().equals("Status 404")) {
            	Metrics.counter("http_requests", HTTP_STATUS_TAG, "404").increment();
                return ResponseEntity.notFound().build();
            }
            UserDto dto = new UserDto(response.getUser());
            Metrics.counter("http_requests", HTTP_STATUS_TAG, "200").increment();
            return ResponseEntity.ok(dto);
        } catch (StatusRuntimeException ex) {
            loggerService.grpcConnectionFailed(request.getMethod(), request.getRequestURI());
            Metrics.counter("http_requests", HTTP_STATUS_TAG, "500").increment();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN_PERMISSION')")
    @GetMapping
    public ResponseEntity<HttpStatus> admin() {
    	Metrics.counter("http_requests", HTTP_STATUS_TAG, "200").increment();
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('GET_EVENTS_PERMISSION')")
    @GetMapping(value = "/events")
    public ResponseEntity<List<String>> getEvents() {
        List<String> events = new ArrayList<>();
        for(String event : userService.getEvents().getEventsList()){
            events.add(event);
        }
        return ResponseEntity.ok(events);
    }
}
