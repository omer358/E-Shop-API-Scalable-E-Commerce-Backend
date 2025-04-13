package com.omo.shop.auth.service;

import com.omo.shop.auth.request.AuthRequest;
import com.omo.shop.security.service.JwtService;
import com.omo.shop.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("should return token when authentication is successful")
    void authenticate_shouldReturnToken_whenCredentialsAreValid() {
        // Given
        AuthRequest request = new AuthRequest("test@example.com", "password123");
        String expectedToken = "mocked-jwt-token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtService.generateTokenForUser(authentication)).thenReturn(expectedToken);

        // When
        String actualToken = authService.authenticate(request);

        // Then
        assertEquals(expectedToken, actualToken);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateTokenForUser(authentication);
    }

    @Test
    @DisplayName("should throw exception when authentication fails")
    void authenticate_shouldThrowException_whenAuthenticationFails() {
        AuthRequest request = new AuthRequest("test@example.com", "wrongpass");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        assertThrows(RuntimeException.class, () -> authService.authenticate(request));
        verify(jwtService, never()).generateTokenForUser(any());
    }

}
