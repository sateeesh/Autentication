package com.assignment.authentication.controller;

import com.assignment.authentication.model.LoginAttempt;
import com.assignment.authentication.model.LoginRequest;
import com.assignment.authentication.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Map<String, Object> login(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            logger.info("Received login request for username: {}", loginRequest.getUsername());

            String message = authenticationService.authenticate(loginRequest);
            logger.info("Authentication successful for user: {}", loginRequest.getUsername());

            response.put("message", message);

            return response;
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}", loginRequest.getUsername(), e);
            response.put("error", "Invalid credentials");
            return response;

        }
    }

    @GetMapping("/login-attempts")
    public List<LoginAttempt> getAllLoginAttempts() {
        return authenticationService.getAllLoginAttempts();
    }
}
