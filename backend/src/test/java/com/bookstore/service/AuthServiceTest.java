package com.bookstore.service;

import com.bookstore.dto.auth.LoginRequest;
import com.bookstore.dto.auth.RegisterRequest;
import com.bookstore.dto.auth.UserResponse;
import com.bookstore.entity.User;
import com.bookstore.exception.UserAlreadyExistsException;
import com.bookstore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest request;

    @BeforeEach
    void setUp() {

        request = new RegisterRequest(
                "John Doe",
                "john@example.com",
                "password123"
        );
    }

    @Test
    void shouldRegisterUser() {

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(false);

        when(passwordEncoder.encode(request.password()))
                .thenReturn("encodedPassword");

        User user = new User(
                request.name(),
                request.email(),
                "encodedPassword"
        );
        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserResponse response = authService.register(request);
        assertNotNull(response);
        assertEquals("John Doe", response.name());
        assertEquals("john@example.com", response.email());

        verify(passwordEncoder).encode(request.password());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldNotRegisterUserWhenEmailAlreadyExists() {

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(true);

        assertThrows(
                UserAlreadyExistsException.class,
                () -> authService.register(request)
        );

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void shouldLoginUser() {

        LoginRequest loginRequest = new LoginRequest(
                "john@example.com",
                "password123"
        );

        Authentication authentication = mock(Authentication.class);

        when(authentication.getName())
                .thenReturn("john@example.com");

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        User user = new User(
                "John Doe",
                "john@example.com",
                "encodedPassword"
        );

        when(userRepository.findByEmail(loginRequest.email()))
                .thenReturn(Optional.of(user));

        UserResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("John Doe", response.name());
        assertEquals("john@example.com", response.email());

        verify(authenticationManager).authenticate(any());
        verify(userRepository).findByEmail(loginRequest.email());
    }

    @Test
    void shouldThrowExceptionForInvalidCredentials() {

        LoginRequest loginRequest = new LoginRequest(
                "john@example.com",
                "wrongPassword"
        );
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));
        assertThrows(
                BadCredentialsException.class,
                () -> authService.login(loginRequest)
        );
        verify(userRepository, never()).findByEmail(anyString());
    }
}