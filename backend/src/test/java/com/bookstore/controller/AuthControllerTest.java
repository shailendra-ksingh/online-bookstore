package com.bookstore.controller;

import com.bookstore.dto.auth.UserResponse;
import com.bookstore.exception.GlobalExceptionHandler;
import com.bookstore.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void shouldRegisterUserAndReturn201Created() throws Exception {

        UserResponse response = new UserResponse(
                1L,
                "John Doe",
                "john@example.com"
        );

        when(authService.register(any()))
                .thenReturn(response);

        String request = """
                {
                    "name": "John Doe",
                    "email": "john@example.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void shouldReturn400WhenRegistrationRequestIsInvalid()
            throws Exception {

        String request = """
                {
                    "name": "",
                    "email": "invalid-email",
                    "password": "short"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.password").exists());
    }

    @Test
    void shouldReturn401WhenLoginCredentialsAreInvalid()
            throws Exception {

        when(authService.login(any()))
                .thenThrow(new org.springframework.security.authentication
                        .BadCredentialsException("Invalid credentials"));

        String request = """
                {
                    "email": "john@example.com",
                    "password": "wrongPassword"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value("Invalid email or password"));
    }
}