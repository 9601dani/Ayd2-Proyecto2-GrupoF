package com.codenbugs.gateway.utils.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token.expiration}")
    private Integer accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private Integer refreshTokenExpiration;

    @Value("${jwt.zone}")
    private String zone;

    @Bean
    public TokenSettings tokenSettings() {
        return new TokenSettings(jwtSecret, accessTokenExpiration, refreshTokenExpiration, zone);
    }
}
