package com.codenbugs.ms_project.exceptions.cases;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CaseNotFoundException extends CaseException {

    public CaseNotFoundException(String message) {
        super(message);
    }
}
