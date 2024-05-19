package com.assignment.authentication;

import com.assignment.authentication.controller.AuthenticationController;
import com.assignment.authentication.exception.InvalidCredentialsException;
import com.assignment.authentication.model.LoginAttempt;
import com.assignment.authentication.model.LoginRequest;
import com.assignment.authentication.repository.LoginAttemptRepository;
import com.assignment.authentication.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthenticationServiceApplicationTests {

	private MockMvc mockMvc;

	@MockBean
	private AuthenticationService authenticationService;

	@MockBean
	private LoginAttemptRepository loginAttemptRepository;

	@Autowired
	private AuthenticationController authenticationController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void testAdminLogin() throws Exception {
		when(authenticationService.authenticate(any(LoginRequest.class))).thenReturn("Hey welcome Admin");

		mockMvc.perform(post("/api/v1/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"admin\", \"password\":\"admin\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Hey welcome Admin"));
	}

	@Test
	public void testGoogleLogin() throws Exception {
		when(authenticationService.authenticate(any(LoginRequest.class))).thenReturn("Welcome googleUser, you have chosen Google Authentication");

		mockMvc.perform(post("/api/v1/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"sateesh_googleUser\", \"password\":\"password\", \"authProvider\":\"google\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Welcome googleUser, you have chosen Google Authentication"));
	}

	@Test
	public void testFacebookLogin() throws Exception {
		when(authenticationService.authenticate(any(LoginRequest.class))).thenReturn("Welcome facebookUser, you have chosen Facebook Authentication");

		mockMvc.perform(post("/api/v1/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"sateesh_facebookUser\", \"password\":\"password\", \"authProvider\":\"facebook\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Welcome facebookUser, you have chosen Facebook Authentication"));
	}

	@Test
	public void testInvalidLogin() throws Exception {
		when(authenticationService.authenticate(any(LoginRequest.class))).thenThrow(new InvalidCredentialsException("Invalid credentials"));

		mockMvc.perform(post("/api/v1/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"invalidUser\", \"password\":\"wrongPassword\"}"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.error").value("Invalid credentials"));
	}
	@Test
	public void testGetAllLoginAttempts() throws Exception {
		List<LoginAttempt> loginAttempts = Arrays.asList(
				new LoginAttempt(101L, "admin", "password1", LocalDateTime.now()),
				new LoginAttempt(102L, "sateesh_google", "password2", LocalDateTime.now()),
		        new LoginAttempt(102L, "sateesh_facebook", "password2", LocalDateTime.now())
		);
		when(authenticationService.getAllLoginAttempts()).thenReturn(loginAttempts);

		mockMvc.perform(get("/api/v1/auth/login-attempts"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].username").value("admin"))
				.andExpect(jsonPath("$[1].username").value("sateesh_google"))
				.andExpect(jsonPath("$[2].username").value("sateesh_facebook"));
	}


}
