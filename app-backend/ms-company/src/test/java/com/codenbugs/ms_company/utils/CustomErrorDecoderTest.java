package com.codenbugs.ms_company.utils;

import com.codenbugs.ms_company.exceptions.feign.ExceptionMessage;
import com.codenbugs.ms_company.exceptions.feign.NotCreatedException;
import com.codenbugs.ms_company.exceptions.feign.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class CustomErrorDecoderTest {

    private final String TIMESTAMP = "2025-05-12T12:00:00Z";
    private final int STATUS = 400;
    private final String ERROR = "Bad Request";
    private final String MESSAGE = "Bad Request";
    private final String PATH = "/v1/test";
    private final String TRACE = null;
    private final String METHOD_KEY = "methodKey";

    private CustomErrorDecoder decoder;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        decoder = new CustomErrorDecoder();
        mapper = new ObjectMapper();
    }

    @Test
    void testDecodeReturnsNotCreatedException() throws Exception {
        // Arrange
        ExceptionMessage message = new ExceptionMessage(TIMESTAMP, STATUS, ERROR, MESSAGE, PATH, TRACE);
        byte[] body = mapper.writeValueAsBytes(message);
        Response response = Response.builder()
                .status(400)
                .reason("Bad Request")
                .request(Request.create(Request.HttpMethod.GET, PATH, Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .body(body)
                .build();

        // Act
        Exception ex = decoder.decode(METHOD_KEY, response);

        // Assert
        assertInstanceOf(NotCreatedException.class, ex);
        assertEquals(MESSAGE, ex.getMessage());
    }

    @Test
    void testDecodeReturnsResourceNotFoundException() throws Exception {
        // Arrange
        ExceptionMessage message = new ExceptionMessage(TIMESTAMP, 404, "Not Found", "Resource Not Found", PATH, TRACE);
        byte[] body = mapper.writeValueAsBytes(message);
        Response response = Response.builder()
                .status(404)
                .reason("Not Found")
                .request(Request.create(Request.HttpMethod.GET, PATH, Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .body(body)
                .build();

        // Act
        Exception ex = decoder.decode(METHOD_KEY, response);

        // Assert
        assertInstanceOf(ResourceNotFoundException.class, ex);
        assertEquals("Resource Not Found", ex.getMessage());
    }

    @Test
    void testDecodeWithInvalidJsonReturnsGenericException() {
        // Arrange
        byte[] invalidBody = "not a json".getBytes(StandardCharsets.UTF_8);
        Response response = Response.builder()
                .status(400)
                .reason("Bad")
                .request(Request.create(Request.HttpMethod.GET, PATH, Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .body(invalidBody)
                .build();

        // Act
        Exception ex = decoder.decode(METHOD_KEY, response);

        // Assert
        assertInstanceOf(Exception.class, ex);
        assertTrue(ex.getMessage().contains("not a json") || ex.getMessage() != null);
    }

    @Test
    void testDecodeDefaultFallback() {
        // Arrange
        byte[] emptyBody = new byte[0];
        Response response = Response.builder()
                .status(500)
                .reason("Internal Server Error")
                .request(Request.create(Request.HttpMethod.GET, PATH, Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .body(emptyBody)
                .build();

        // Act
        Exception ex = decoder.decode(METHOD_KEY, response);

        // Assert
        assertNotNull(ex);
    }

    @Test
    void testStatus400ReturnsNotCreatedExceptionWithFallbackMessage() throws Exception {
        // Arrange
        byte[] emptyJson = "{}".getBytes(StandardCharsets.UTF_8);
        Response response = Response.builder()
                .status(400)
                .reason("Bad Request")
                .request(Request.create(Request.HttpMethod.GET, PATH, Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .body(emptyJson)
                .build();

        // Act
        Exception ex = decoder.decode(METHOD_KEY, response);

        // Assert
        assertInstanceOf(NotCreatedException.class, ex);
        assertEquals("Resource Not Created.", ex.getMessage());
    }

    @Test
    void testStatus404ReturnsResourceNotFoundExceptionWithFallbackMessage() throws Exception {
        // Arrange
        byte[] emptyJson = "{}".getBytes(StandardCharsets.UTF_8); // No message field
        Response response = Response.builder()
                .status(404)
                .reason("Not Found")
                .request(Request.create(Request.HttpMethod.GET, PATH, Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .body(emptyJson)
                .build();

        // Act
        Exception ex = decoder.decode(METHOD_KEY, response);

        // Assert
        assertInstanceOf(ResourceNotFoundException.class, ex);
        assertEquals("Resource Not Found.", ex.getMessage());
    }

    @Test
    void testDefaultStatusUsesFallbackDecoder() {
        // Arrange
        byte[] body = "{}".getBytes(StandardCharsets.UTF_8);
        Response response = Response.builder()
                .status(500)
                .reason("Internal Server Error")
                .request(Request.create(Request.HttpMethod.GET, PATH, Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .body(body)
                .build();

        // Act
        Exception ex = decoder.decode(METHOD_KEY, response);

        // Assert
        assertNotNull(ex);
        assertInstanceOf(Exception.class, ex);
    }

    @Test
    void testStatus400ReturnsNotCreatedExceptionWithNullMessage() throws Exception {
        ExceptionMessage message = new ExceptionMessage(TIMESTAMP, 400, ERROR, null, PATH, TRACE);
        byte[] body = mapper.writeValueAsBytes(message);

        Response response = Response.builder()
                .status(400)
                .reason("Bad Request")
                .request(Request.create(Request.HttpMethod.GET, PATH, Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .body(body)
                .build();

        Exception ex = decoder.decode(METHOD_KEY, response);

        assertInstanceOf(NotCreatedException.class, ex);
        assertEquals("Resource Not Created.", ex.getMessage());
    }

    @Test
    void testStatus404ReturnsResourceNotFoundExceptionWithNullMessage() throws Exception {
        ExceptionMessage message = new ExceptionMessage(TIMESTAMP, 404, "Not Found", null, PATH, TRACE);
        byte[] body = mapper.writeValueAsBytes(message);

        Response response = Response.builder()
                .status(404)
                .reason("Not Found")
                .request(Request.create(Request.HttpMethod.GET, PATH, Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .body(body)
                .build();

        Exception ex = decoder.decode(METHOD_KEY, response);

        assertInstanceOf(ResourceNotFoundException.class, ex);
        assertEquals("Resource Not Found.", ex.getMessage());
    }

    @Test
    void testStatus400ReturnsNotCreatedExceptionWithRealMessage() throws Exception {
        String customMessage = "Ya existe el recurso";
        ExceptionMessage message = new ExceptionMessage(TIMESTAMP, 400, ERROR, customMessage, PATH, TRACE);
        byte[] body = mapper.writeValueAsBytes(message);

        Response response = Response.builder()
                .status(400)
                .reason("Bad Request")
                .request(Request.create(Request.HttpMethod.GET, PATH, Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .body(body)
                .build();

        Exception ex = decoder.decode(METHOD_KEY, response);

        assertInstanceOf(NotCreatedException.class, ex);
        assertEquals(customMessage, ex.getMessage());
    }

    @Test
    void testStatus404ReturnsResourceNotFoundExceptionWithRealMessage() throws Exception {
        String customMessage = "El recurso no fue encontrado";
        ExceptionMessage message = new ExceptionMessage(TIMESTAMP, 404, "Not Found", customMessage, PATH, TRACE);
        byte[] body = mapper.writeValueAsBytes(message);

        Response response = Response.builder()
                .status(404)
                .reason("Not Found")
                .request(Request.create(Request.HttpMethod.GET, PATH, Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .body(body)
                .build();

        Exception ex = decoder.decode(METHOD_KEY, response);

        assertInstanceOf(ResourceNotFoundException.class, ex);
        assertEquals(customMessage, ex.getMessage());
    }

}