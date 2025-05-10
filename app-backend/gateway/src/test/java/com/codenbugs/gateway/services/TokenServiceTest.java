package com.codenbugs.gateway.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.codenbugs.gateway.utils.token.TokenSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenServiceTest {

    String token;
    String expiredToken;
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        TokenSettings tokenSettings = new TokenSettings("testSecret", 10, 10, "America/Guatemala");
        tokenService = new TokenService(tokenSettings);
        Algorithm algorithm = Algorithm.HMAC256(tokenSettings.getJwtSecret());
        token = JWT.create()
                .withExpiresAt(Date.from(Instant.now().plusSeconds(1200)))
                .sign(algorithm);

        expiredToken = JWT.create()
                .withExpiresAt(Date.from(Instant.now().minusSeconds(1200)))
                .sign(algorithm);
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
