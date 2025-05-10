package com.codenbugs.ms_user.services.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.codenbugs.ms_user.dto.token.RefreshTokenRequest;
import com.codenbugs.ms_user.dto.token.TokenResponse;
import com.codenbugs.ms_user.exceptions.user.ForbiddenException;
import com.codenbugs.ms_user.exceptions.user.UserException;
import com.codenbugs.ms_user.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_user.models.user.User;
import com.codenbugs.ms_user.repository.UserRepository;
import com.codenbugs.ms_user.utils.token.TokenSettings;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(rollbackOn = UserException.class)
public class TokenService {

    private final TokenSettings tokenSettings;
    private final Algorithm algorithm;
    private final UserRepository userRepository;
    private final JWTVerifier verifier;

    public TokenService(TokenSettings tokenSettings, UserRepository userRepository) {
        this.tokenSettings = tokenSettings;
        this.userRepository = userRepository;
        this.algorithm = Algorithm.HMAC256(tokenSettings.getJwtSecret());
        this.verifier = JWT.require(algorithm).build();

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

    public TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws UserException {
        Integer id = refreshTokenRequest.id();
        String token = refreshTokenRequest.token();

        User user = this.userRepository.findByIdAndToken(id, token).orElseThrow(() -> new UserNotFoundException("User not found"));
        String refreshToken = user.getToken();

        if(this.isTokenExpired(refreshToken)) {
            throw new ForbiddenException("User forbidden");
        }

        String accessToken = this.getAccessToken(user);

        return new TokenResponse(accessToken, refreshToken);
    }

    public DecodedJWT decodedJWT(String token) throws JWTVerificationException {
        return this.verifier.verify(token);
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
            return true;
        }
    }
}
