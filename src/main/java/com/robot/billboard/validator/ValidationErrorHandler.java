package com.robot.billboard.validator;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom ValidationErrorHandler for validation, containing onMethodArgumentNotValidException for args and
 * onConstraintValidationException for objects.
 */
@ControllerAdvice("com.robot.billboard")
public class ValidationErrorHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationError onConstraintValidationException(
            ConstraintViolationException ex) {
        ValidationError error = new ValidationError(
                "Validation failure: " + ex.getConstraintViolations().size() + " errors.");

        ex.getConstraintViolations().forEach((violation) -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            error.addValidationError(fieldName + " " + errorMessage);
        });
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> onMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<?> handleConflict(RuntimeException ex) {
        return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Unknown state: UNSUPPORTED_STATUS"));
    }
}
