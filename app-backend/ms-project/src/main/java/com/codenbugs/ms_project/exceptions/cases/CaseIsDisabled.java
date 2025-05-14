package com.codenbugs.ms_project.exceptions.cases;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CaseIsDisabled extends CaseException {
    public CaseIsDisabled(String message) {
        super(message);
    }
}
