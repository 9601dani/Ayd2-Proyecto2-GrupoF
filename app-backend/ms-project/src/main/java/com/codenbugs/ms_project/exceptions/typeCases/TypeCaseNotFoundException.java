package com.codenbugs.ms_project.exceptions.typeCases;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TypeCaseNotFoundException extends TypeCasesException {
    public TypeCaseNotFoundException(String message) {
        super(message);
    }
}
