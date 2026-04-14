package com.example.demochatgpt.unit;

import com.example.demochatgpt.models.User;
import com.example.demochatgpt.utils.JwtConfig;
import com.example.demochatgpt.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    @Mock
    private JwtConfig jwtConfig;

    private JwtUtils jwtUtils;

    private final String SECRET = "mysecretkeymysecretkeymysecretkey123"; // mínimo 256 bits

    @BeforeEach
    void setUp() {
        when(jwtConfig.getSecret()).thenReturn(SECRET);

        jwtUtils = new JwtUtils(jwtConfig);
        jwtUtils.init(); // IMPORTANTE: simula @PostConstruct
    }

    @Test
    void shouldGenerateValidToken() {
        User user = new User();
        user.setEmail("test@example.com");

        String token = jwtUtils.generateToken(user);

        assertNotNull(token);
        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    void shouldExtractUsernameFromToken() {
        User user = new User();
        user.setEmail("test@example.com");

        String token = jwtUtils.generateToken(user);

        String username = jwtUtils.extractUsername(token);

        assertEquals("test@example.com", username);
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        String invalidToken = "esto.no.es.un.jwt";

        boolean isValid = jwtUtils.validateToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    void shouldFailIfTokenIsTampered() {
        User user = new User();
        user.setEmail("test@example.com");

        String token = jwtUtils.generateToken(user);

        // alteramos el token
        String tamperedToken = token + "abc";

        assertFalse(jwtUtils.validateToken(tamperedToken));
    }
}