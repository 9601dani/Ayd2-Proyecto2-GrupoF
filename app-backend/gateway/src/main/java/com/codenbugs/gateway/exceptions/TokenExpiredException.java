package com.codenbugs.gateway.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenExpiredException extends UserException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
