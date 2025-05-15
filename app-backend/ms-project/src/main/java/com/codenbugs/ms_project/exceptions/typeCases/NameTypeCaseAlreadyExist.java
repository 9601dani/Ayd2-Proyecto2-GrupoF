package com.codenbugs.ms_project.exceptions.typeCases;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NameTypeCaseAlreadyExist extends TypeCasesException {
    public NameTypeCaseAlreadyExist(String message) {
        super(message);
    }
}
