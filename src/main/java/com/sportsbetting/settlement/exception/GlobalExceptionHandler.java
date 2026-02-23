package com.sportsbetting.settlement.exception;

import com.sportsbetting.settlement.api.dto.Error;
import com.sportsbetting.settlement.api.dto.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handling for REST APIs. Returns consistent Response with errors.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Void>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<Error> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> Error.builder()
                        .message(err.getDefaultMessage())
                        .code(ExceptionConstant.ERROR_CODE_VALIDATION)
                        .field(err.getField())
                        .build())
                .collect(Collectors.toList());

        log.warn("Validation failed for {}: {}", request.getRequestURI(), errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.failure(HttpStatus.BAD_REQUEST.value(), errors));
    }

    @ExceptionHandler(BetNotFoundException.class)
    public ResponseEntity<Response<Void>> handleBetNotFound(BetNotFoundException ex, HttpServletRequest request) {
        log.debug("Bet not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Response.failure(HttpStatus.NOT_FOUND.value(), ex.getMessage(), ExceptionConstant.ERROR_CODE_NOT_FOUND));
    }

    @ExceptionHandler(DuplicateBetException.class)
    public ResponseEntity<Response<Void>> handleDuplicateBet(DuplicateBetException ex, HttpServletRequest request) {
        log.warn("Duplicate bet: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Response.failure(HttpStatus.CONFLICT.value(), ex.getMessage(), ExceptionConstant.ERROR_CODE_CONFLICT));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<Void>> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Bad request for {}: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.failure(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), ExceptionConstant.ERROR_CODE_BAD_REQUEST));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response<Void>> handleMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String message = ex.getCause() != null ? ex.getCause().getMessage() : ExceptionConstant.MESSAGE_MALFORMED_REQUEST_BODY;
        log.warn("Unreadable request body for {}: {}", request.getRequestURI(), message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.failure(HttpStatus.BAD_REQUEST.value(),
                        ExceptionConstant.MESSAGE_INVALID_REQUEST_BODY_PREFIX + message,
                        ExceptionConstant.ERROR_CODE_BAD_REQUEST));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Response<Void>> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        log.warn("Missing parameter {} for {}", ex.getParameterName(), request.getRequestURI());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.failure(HttpStatus.BAD_REQUEST.value(),
                        ExceptionConstant.MESSAGE_MISSING_PARAMETER_PREFIX + ex.getParameterName(),
                        ExceptionConstant.ERROR_CODE_BAD_REQUEST));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Response<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = String.format(ExceptionConstant.FORMAT_INVALID_PARAMETER_VALUE, ex.getValue(), ex.getName());
        log.warn("Type mismatch for {}: {}", request.getRequestURI(), message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.failure(HttpStatus.BAD_REQUEST.value(), message, ExceptionConstant.ERROR_CODE_BAD_REQUEST));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Unhandled error for {}: ", request.getRequestURI(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ExceptionConstant.MESSAGE_UNEXPECTED_ERROR,
                        ExceptionConstant.ERROR_CODE_INTERNAL));
    }
}
