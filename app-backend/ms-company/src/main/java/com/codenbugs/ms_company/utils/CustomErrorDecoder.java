package com.codenbugs.ms_company.utils;

import com.codenbugs.ms_company.exceptions.feign.ExceptionMessage;
import com.codenbugs.ms_company.exceptions.feign.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.io.InputStream;

public class CustomErrorDecoder implements ErrorDecoder {

    private ErrorDecoder errorDecoder = new ErrorDecoder.Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        ExceptionMessage exceptionMessage = null;
        try(InputStream stream = response.body().asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            exceptionMessage = mapper.readValue(stream, ExceptionMessage.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }

        return switch (response.status()) {
            case 404 -> new ResourceNotFoundException(exceptionMessage != null ? exceptionMessage.getMessage() : "Resource Not Found.");
            default -> errorDecoder.decode(methodKey, response);
        };
    }
}
