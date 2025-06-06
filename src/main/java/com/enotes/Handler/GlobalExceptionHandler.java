package com.enotes.Handler;

import com.enotes.Exception.*;
import com.enotes.Util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.FileNotFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("Unhandled exception: ", e);
        return CommonUtil.createErrorResponseMessage("An unexpected error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> nullPointerException(NullPointerException e) {
        log.error("Null pointer exception: ", e);
        return CommonUtil.createErrorResponseMessage("Something went wrong. Please contact support.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(SuccessException.class)
    public ResponseEntity<?> nullPointerException(SuccessException e) {
        log.error("Success Message exception: ", e);
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.OK);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> missingParameterException(MissingServletRequestParameterException e) {
        log.error("Missing request parameter: {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage("Missing parameter: " + e.getParameterName(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException e) {
        log.error("Resource not found: {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> accessDeniedException(AccessDeniedException e) {
        log.error("Access denied: {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage("You do not have permission to perform this action.", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExistDataException.class)
    public ResponseEntity<?> existDataException(ExistDataException e) {
        log.error("Data already exists: {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> validationException(ValidationException e) {
        log.error("Custom validation error: {}", e.getMessage());
        return CommonUtil.createErrorResponse(e.getErrors(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldError() != null
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : "Validation failed";
        log.error("Validation failed: {}", errorMessage);
        return CommonUtil.createErrorResponseMessage("Validation failed: " + errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("Malformed request body: {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage("Malformed JSON request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgumentException(IllegalArgumentException e) {
        log.error("Illegal argument: {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> fileNotFoundException(FileNotFoundException e) {
        log.error("File not found: {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentialsException(BadCredentialsException e) {
        log.error("Bad credentials: {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtTokenExpiredException.class)
    public ResponseEntity<?> jwtTokenExpiredException(JwtTokenExpiredException e) {
        log.error("JWT token expired: {}", e.getMessage());
        return CommonUtil.createErrorResponseMessage("Session expired. Please log in again.", HttpStatus.UNAUTHORIZED);
    }
}
