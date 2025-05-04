package com.codenbugs.ms_user.services.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.codenbugs.ms_user.dto.user.TokenResponse;
import com.codenbugs.ms_user.models.user.User;
import com.codenbugs.ms_user.utils.token.TokenSettings;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    private final TokenSettings tokenSettings;

    private final Algorithm algorithm;

    public TokenService(TokenSettings tokenSettings) {
        this.tokenSettings = tokenSettings;
        this.algorithm = Algorithm.HMAC256(tokenSettings.getJwtSecret());
    }

    private String generateToken(Map<String, Object> claims, Integer minutes) {
        return JWT.create()
                .withIssuer("codenbugs")
                .withClaim("claims", claims)
                .withExpiresAt(getInstant(minutes))
                .sign(this.algorithm);
    }

    private Instant getInstant(Integer minutes) {
        ZoneId zone = ZoneId.of(tokenSettings.getZone());
        return LocalDateTime.now().plusMinutes(minutes).atZone(zone).toInstant();
    }

    private String getAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        return generateToken(claims, tokenSettings.getAccessTokenExpiration());
    }

    private String getRefreshToken() {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, tokenSettings.getRefreshTokenExpiration());
    }

    public TokenResponse getTokens(User user) {
        String accessToken = getAccessToken(user);
        String refreshToken = getRefreshToken();
        return new TokenResponse(accessToken, refreshToken);
    }
}
