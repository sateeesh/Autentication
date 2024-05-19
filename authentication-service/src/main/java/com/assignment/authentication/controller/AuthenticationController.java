package com.assignment.authentication.controller;

import com.assignment.authentication.exception.InvalidCredentialsException;
import com.assignment.authentication.model.LoginAttempt;
import com.assignment.authentication.model.LoginRequest;
import com.assignment.authentication.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            logger.info("Received login request for username: {}", loginRequest.getUsername());

            String message = authenticationService.authenticate(loginRequest);
            logger.info("Authentication successful for user: {}", loginRequest.getUsername());

            response.put("message", message);

            return ResponseEntity.ok(response);
        } catch (InvalidCredentialsException e) {
            logger.error("Authentication failed for user: {}", loginRequest.getUsername(), e);
            response.put("error", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during authentication for user: {}", loginRequest.getUsername(), e);
            response.put("error", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/login-attempts")
    public List<LoginAttempt> getAllLoginAttempts() {
        return authenticationService.getAllLoginAttempts();
    }
}
