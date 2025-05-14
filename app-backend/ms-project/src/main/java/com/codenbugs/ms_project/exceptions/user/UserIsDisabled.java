package com.codenbugs.ms_project.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserIsDisabled extends Exception {
    public UserIsDisabled(String message) {
        super(message);
    }
}
