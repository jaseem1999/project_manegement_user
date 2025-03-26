package com.pm.user.project_management.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return buildErrorResponse("Validation failed", "Invalid fields detected", HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage())
        );

        return buildErrorResponse("Validation failed", "Constraint violations detected", HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("Invalid value for parameter '%s': '%s'. Please provide a valid value.",
                ex.getName(), ex.getValue());

        return buildErrorResponse("Type Mismatch", message, HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(UserForbiddenException.class)
    public ResponseEntity<Map<String, Object>> handleUserForbidden(UserForbiddenException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message","User forbidden....");
        return buildErrorResponse("Type Mismatch", ex.getMessage(), HttpStatus.FORBIDDEN, errors);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<Map<String, Object>> handleJsonParseException(JsonParseException ex) {
        return buildErrorResponse("Invalid JSON", "Invalid JSON format: " + ex.getOriginalMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<Map<String, Object>> handleJsonMappingException(JsonMappingException ex) {
        return buildErrorResponse("Invalid Input", "Invalid input value: " + ex.getOriginalMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String,Object>> handleRunTimeException(RuntimeException ex){
        return buildErrorResponse("Internal run time error",
                ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR,
                Map.of("message", ex.getMessage()));
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String title, String detail, HttpStatus status, Map<String, String> errors) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("title", title);
        response.put("detail", detail);
        response.put("errors", errors);
        response.put("status", status.value());
        return new ResponseEntity<>(response, status);
    }
}
