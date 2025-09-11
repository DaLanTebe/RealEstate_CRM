package com.crm.corecrm.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidationException(ConstraintViolationException exception) {
        String errorMessage = exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("Validation error occurred.");

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UserAlreadyExistsException.class, UserNotFoundException.class})
    public ResponseEntity<String> handleValidationException(RuntimeException exception) {

        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
