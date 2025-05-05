package com.codenbugs.gateway.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends UserException {
    public ForbiddenException(String message) {
        super(message);
    }
}
