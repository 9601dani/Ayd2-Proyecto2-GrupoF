package com.codenbugs.ms_project.exceptions.typeCases;

import java.util.concurrent.ExecutionException;

public class PhaseCasesException extends ExecutionException {
    public PhaseCasesException(String message) {
        super(message);
    }
}
