package com.apigateway.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthcheckController {
	public HealthcheckController() {
    }

    /**
     * GET /health
     *
     * @return all available servers
     */
    @GetMapping
    public ResponseEntity<String> health() throws IOException {
        return new ResponseEntity<>("healthy", HttpStatus.OK);
    }
}
