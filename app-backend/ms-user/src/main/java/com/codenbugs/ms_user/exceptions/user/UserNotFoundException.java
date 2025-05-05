package com.codenbugs.ms_user.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends UserException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
