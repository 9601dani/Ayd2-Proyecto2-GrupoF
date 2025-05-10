package com.codenbugs.ms_user.services.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.codenbugs.ms_user.repository.UserRepository;
import com.codenbugs.ms_user.utils.token.TokenSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    String token;
    String expiredToken;
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        TokenSettings tokenSettings = new TokenSettings("testSecret", 10, 10, "America/Guatemala");
        tokenService = new TokenService(tokenSettings, userRepository);
        Algorithm algorithm = Algorithm.HMAC256(tokenSettings.getJwtSecret());
        token = JWT.create()
                .withExpiresAt(Date.from(Instant.now().plusSeconds(1200)))
                .sign(algorithm);

        expiredToken = JWT.create()
                .withExpiresAt(Date.from(Instant.now().minusSeconds(1200)))
                .sign(algorithm);
    }

    @Test
    void getTokens() {
    }

    @Test
    void refreshToken() {
    }

    @Test
    void decodedJWT() {
    }

    @Test
    void shouldNotBeExpired() {
        boolean isExpired = tokenService.isTokenExpired(token);
        assertFalse(isExpired);
    }

    @Test
    void shouldBeTokenExpired() {
        boolean isExpired = tokenService.isTokenExpired(expiredToken);
        assertTrue(isExpired);
    }
}