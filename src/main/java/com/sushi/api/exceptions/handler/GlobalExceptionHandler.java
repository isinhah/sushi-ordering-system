package com.sushi.api.exceptions.handler;

import com.sushi.api.exceptions.BadRequestException;
import com.sushi.api.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handlerResourceNotFoundException(ResourceNotFoundException ex) {
        ExceptionResponse response = new ExceptionResponse(
                "Resource Not Found Exception",
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                ex.getClass().getName(),
                LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handlerBadRequestException(BadRequestException ex) {
        ExceptionResponse response = new ExceptionResponse(
                "Bad Request Exception",
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                ex.getClass().getName(),
                LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        ExceptionResponse response = new ExceptionResponse(
                "Argument Type Mismatch",
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                ex.getClass().getName(),
                LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionDetails> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

        ValidationExceptionDetails response = new ValidationExceptionDetails(
                "Method Argument Not Valid",
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed for one or more fields.",
                ex.getClass().getName(),
                LocalDateTime.now(),
                fields,
                fieldsMessage
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionResponse> handlerNullPointerException(NullPointerException ex) {
        ExceptionResponse response = new ExceptionResponse(
                "Null Pointer Exception",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                ex.getClass().getName(),
                LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ExceptionResponse response = new ExceptionResponse(
                "Data Integrity Violation",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Data integrity violation error occurred.",
                ex.getClass().getName(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        ExceptionResponse response = new ExceptionResponse(
                "No Resource Found Exception",
                HttpStatus.NOT_FOUND.value(),
                "The requested resource was not found.",
                ex.getClass().getName(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}