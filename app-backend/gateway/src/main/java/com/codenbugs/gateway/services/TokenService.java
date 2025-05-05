package com.codenbugs.gateway.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.codenbugs.gateway.exceptions.TokenExpiredException;
import com.codenbugs.gateway.utils.token.TokenSettings;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class TokenService {

    private final TokenSettings tokenSettings;

    private final JWTVerifier verifier;

    public TokenService(TokenSettings tokenSettings) {
        this.tokenSettings = tokenSettings;
        Algorithm algorithm = Algorithm.HMAC256(tokenSettings.getJwtSecret());
        this.verifier = JWT.require(algorithm).build();
    }

    public DecodedJWT decodedJWT(String token) throws JWTVerificationException {
        return this.verifier.verify(token);
    }

    public String getClaim(String token, String claimName) throws JWTVerificationException {
        DecodedJWT decodedJWT = this.decodedJWT(token);
        return decodedJWT.getClaim(claimName).asString();
    }

    private LocalDateTime getExpiredAtFromToken(String token) throws JWTVerificationException {
        DecodedJWT decodedJWT = this.decodedJWT(token);
        return decodedJWT.getExpiresAt().toInstant().atZone(ZoneId.of(this.tokenSettings.getZone())).toLocalDateTime();
    }

    public boolean isTokenExpired(String token) {
        try {
            LocalDateTime expiredAt = this.getExpiredAtFromToken(token);
            return LocalDateTime.now().isAfter(expiredAt);
        } catch (JWTVerificationException e) {
            System.out.println("JWT verification failed");
            return true;
        }
    }
}
