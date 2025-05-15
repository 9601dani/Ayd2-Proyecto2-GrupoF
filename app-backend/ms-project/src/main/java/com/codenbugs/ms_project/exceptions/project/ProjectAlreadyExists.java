package com.codenbugs.ms_project.exceptions.project;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProjectAlreadyExists extends ProjectException {
    public ProjectAlreadyExists(String message) {
        super(message);
    }
}
