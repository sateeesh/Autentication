package com.assignment.authentication.service;

import com.assignment.authentication.exception.InvalidCredentialsException;
import com.assignment.authentication.model.LoginAttempt;
import com.assignment.authentication.model.LoginRequest;
import com.assignment.authentication.repository.LoginAttemptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private LoginAttemptRepository loginAttemptRepository;

    public String authenticate(LoginRequest loginRequest) {
        logger.info("Authentication attempt for username: {}", loginRequest.getUsername());

        if ("admin".equals(loginRequest.getUsername()) && "admin".equals(loginRequest.getPassword())) {
            logSuccessfulAttempt("admin");
            logger.info("Admin login successful ");
            return "Hey welcome Admin";
        } else if ("google".equals(loginRequest.getAuthProvider())) {
            logSuccessfulAttempt(loginRequest.getUsername());
            logger.info("Google authentication successful for username: {} ", loginRequest.getUsername());
            return "Welcome " + loginRequest.getUsername() + ", you have chosen Google Authentication";
        } else if ("facebook".equals(loginRequest.getAuthProvider())) {
            logSuccessfulAttempt(loginRequest.getUsername());
            logger.info("Facebook authentication successful for username: {} ", loginRequest.getUsername());
            return "Welcome " + loginRequest.getUsername() + ", you have chosen Facebook Authentication";
        } else {
            logInvalidAttempt(loginRequest);
            logger.warn("Invalid credentials for username: {} ", loginRequest.getUsername());
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }

    private void logSuccessfulAttempt(String username) {
        LoginAttempt attempt = new LoginAttempt();
        attempt.setUsername(username);
        attempt.setTimestamp(LocalDateTime.now());
        //attempt.setIpAddress(request.getRemoteAddr());
        loginAttemptRepository.save(attempt);
        logger.debug("Logged successful attempt for username: {}", username);
    }

    private void logInvalidAttempt(LoginRequest loginRequest) {
        LoginAttempt attempt = new LoginAttempt();
        attempt.setUsername(loginRequest.getUsername());
        attempt.setPassword(loginRequest.getPassword());
        attempt.setTimestamp(LocalDateTime.now());
        //attempt.setIpAddress(request.getRemoteAddr());
        loginAttemptRepository.save(attempt);
        logger.debug("Logged invalid attempt for username: {}", loginRequest.getUsername());
    }

    public List<LoginAttempt> getAllLoginAttempts() {
        return loginAttemptRepository.findAll();
    }
}
