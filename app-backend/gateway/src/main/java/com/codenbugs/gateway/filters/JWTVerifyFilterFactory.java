package com.codenbugs.gateway.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.codenbugs.gateway.exceptions.ForbiddenException;
import com.codenbugs.gateway.exceptions.TokenExpiredException;
import com.codenbugs.gateway.services.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JWTVerifyFilterFactory extends AbstractGatewayFilterFactory<JWTVerifyFilterFactory.Config> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public final TokenService tokenService;

    public JWTVerifyFilterFactory(
            TokenService tokenService
    ) {
        super(Config.class);
        this.tokenService = tokenService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String authHeader = request.getHeaders().getFirst("Authorization");

            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ForbiddenException("Token not found");
            }

            String token = authHeader.substring(7);
            if(this.tokenService.isTokenExpired(token)) {
                throw new TokenExpiredException("Token expired");
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {}
}
