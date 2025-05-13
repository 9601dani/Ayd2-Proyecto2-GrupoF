package com.codenbugs.ms_user.services.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.codenbugs.ms_user.dto.token.RefreshTokenRequest;
import com.codenbugs.ms_user.dto.token.TokenResponse;
import com.codenbugs.ms_user.exceptions.user.ForbiddenException;
import com.codenbugs.ms_user.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_user.models.user.User;
import com.codenbugs.ms_user.repository.UserRepository;
import com.codenbugs.ms_user.utils.token.TokenSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    private String validToken;
    private String expiredToken;
    private final Integer ID = 1;
    private final String USERNAME = "username";
    private final String EMAIL = "email";
    private final String SECRET = "testSecret";
    private final String ZONE = "America/Guatemala";
    private final int ACCESS_EXP_MIN = 10;
    private final int REFRESH_EXP_MIN = 10;
    private final String ISSUER = "testIssuer";
    private final Integer EXPIRES_IN = 3600;

    private User user;
    private RefreshTokenRequest refreshTokenRequest;

    @Mock
    private UserRepository userRepository;

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        TokenSettings tokenSettings = new TokenSettings(SECRET, ACCESS_EXP_MIN, REFRESH_EXP_MIN, ZONE);
        tokenService = new TokenService(tokenSettings, userRepository);

        validToken = generateToken(ISSUER, EXPIRES_IN);
        expiredToken = generateToken(ISSUER, -EXPIRES_IN);

        user = new User();
        user.setId(ID);
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);

        refreshTokenRequest = new RefreshTokenRequest(ID, validToken);
    }


    @Test
    void getTokensGeneratesAccessAndRefreshToken() {
        // Act
        TokenResponse response = tokenService.getTokens(user);

        // Assert
        assertNotNull(response);
        assertNotNull(response.accessToken());
        assertNotNull(response.refreshToken());
        assertNotEquals(response.accessToken(), response.refreshToken());
    }

    @Test
    void decodedJWTReturnsValidDecodedObject() {
        // Act
        DecodedJWT decoded = tokenService.decodedJWT(validToken);

        // Assert
        assertNotNull(decoded);
        assertEquals(validToken, decoded.getToken());
    }
    @Test
    void isTokenExpiredReturnsFalseForValidToken() {
        // Act
        boolean isExpired = tokenService.isTokenExpired(validToken);

        // Assert
        assertFalse(isExpired);
    }

    @Test
    void isTokenExpiredReturnsTrueForExpiredToken() {
        // Act
        boolean isExpired = tokenService.isTokenExpired(expiredToken);

        // Assert
        assertTrue(isExpired);
    }

    @Test
    void refreshTokenValidReturnsNewAccessToken() throws Exception {
        // Arrange
        user.setToken(validToken);
        when(userRepository.findByIdAndToken(user.getId(), validToken)).thenReturn(Optional.of(user));

        // Act
        TokenResponse response = tokenService.refreshToken(refreshTokenRequest);

        // Assert
        assertNotNull(response);
        assertEquals(validToken, response.refreshToken());
        assertNotNull(response.accessToken());
    }


    @Test
    void refreshTokenExpiredThrowsForbidden() {
        // Arrange
        user.setToken(expiredToken);
        RefreshTokenRequest request = new RefreshTokenRequest(user.getId(), expiredToken);
        when(userRepository.findByIdAndToken(user.getId(), expiredToken)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(ForbiddenException.class, () -> tokenService.refreshToken(request));
    }


    @Test
    void refreshTokenUserNotFoundThrowsUserNotFoundException() {
        // Arrange
        when(userRepository.findByIdAndToken(user.getId(), validToken)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> tokenService.refreshToken(refreshTokenRequest));
    }


    private String generateToken(String issuer, long secondsFromNow) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        return JWT.create()
                .withIssuer(issuer)
                .withExpiresAt(Date.from(Instant.now().plusSeconds(secondsFromNow)))
                .sign(algorithm);
    }
}