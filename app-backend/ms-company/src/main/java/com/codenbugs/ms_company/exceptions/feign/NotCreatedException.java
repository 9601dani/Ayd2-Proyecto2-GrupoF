package com.codenbugs.ms_company.exceptions.feign;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotCreatedException extends Exception {

    public NotCreatedException(String message) {
        super(message);
    }
}
