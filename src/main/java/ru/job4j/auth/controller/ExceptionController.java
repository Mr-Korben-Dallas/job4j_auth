package ru.job4j.auth.controller;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.job4j.auth.exception.ApiError;
import ru.job4j.auth.exception.LocalizedException;

@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleDataIntegrityViolationException(final DataIntegrityViolationException ex) {
        ConstraintViolationException cause = (ConstraintViolationException) ex.getCause();
        String failedField = cause.getConstraintName();
        LocalizedException localizedException = new LocalizedException(failedField);
        String exceptionMessage = localizedException.getLocalizedMessage();
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exceptionMessage, failedField);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
