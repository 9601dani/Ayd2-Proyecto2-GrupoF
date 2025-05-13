package com.codenbugs.ms_project.exceptions.comment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CommentNotCreatedException extends CommentException {
    public CommentNotCreatedException(String message) {
        super(message);
    }
}
