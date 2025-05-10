package com.codenbugs.gateway.filters;

import com.codenbugs.gateway.exceptions.UserException;
import com.codenbugs.gateway.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JWTVerifyFilterFactoryTest {

    private final String VALID_AUTHHEADER = "Bearer VALID_TOKEN";
    private final String VALID_TOKEN = "VALID_TOKEN";
    private final String INVALID_AUTHHEADER = "Beare INVALID_TOKEN";

    @Mock
    private TokenService tokenService;

    @Mock
    private ServerHttpRequest request;

    @Mock
    private ServerWebExchange exchange;

    @Mock
    private GatewayFilterChain filterChain;

    @Mock
    private HttpHeaders headers;

    @InjectMocks
    private JWTVerifyFilterFactory factory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(this.exchange.getRequest()).thenReturn(this.request);
        when(this.request.getHeaders()).thenReturn(this.headers);
    }

    @Test
    void shouldFilter() {

        when(this.headers.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn(VALID_AUTHHEADER);
        when(this.tokenService.isTokenExpired(VALID_TOKEN)).thenReturn(false);

        when(this.filterChain.filter(exchange)).thenReturn(Mono.empty());
        this.factory.apply(new JWTVerifyFilterFactory.Config()).filter(exchange, filterChain);

        verify(this.filterChain, times(1)).filter(exchange);

    }

    @Test
    void shouldNotFilterForbiddenRequestNotBearer() {
        when(this.headers.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn(INVALID_AUTHHEADER);

        assertThrows(UserException.class, () -> {
            this.factory.apply(new JWTVerifyFilterFactory.Config()).filter(exchange, filterChain);
        });

    }

    @Test
    void shouldNotFilterForbiddenRequestNull() {
        when(this.headers.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        assertThrows(UserException.class, () -> {
            this.factory.apply(new JWTVerifyFilterFactory.Config()).filter(exchange, filterChain);
        });

    }

    @Test
    void shouldTokenBeExpired() {

        when(this.headers.getFirst(HttpHeaders.AUTHORIZATION)).thenReturn(VALID_AUTHHEADER);
        when(this.tokenService.isTokenExpired(VALID_TOKEN)).thenReturn(true);

        assertThrows(UserException.class, () -> {
            this.factory.apply(new JWTVerifyFilterFactory.Config()).filter(exchange, filterChain);
        });


    }

}