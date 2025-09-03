package org.example.advice;

import org.example.exception.BadRequestException;
import org.example.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(EntityNotFoundException exception) {
        var responseBody = buildResponseBody(HttpStatus.NOT_FOUND, exception.getMessage());

        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException exception) {
        var responseBody = buildResponseBody(HttpStatus.BAD_REQUEST, exception.getMessage());

        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception exception) {
        var responseBody = buildResponseBody(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());

        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> buildResponseBody(HttpStatus errorStatus, String errorMessage) {
        var body = new HashMap<String, Object>();

        body.put("timestamp", LocalDateTime.now());
        body.put("status", errorStatus.value());
        body.put("error", errorStatus);
        body.put("message", errorMessage);

        return body;

    }
}
