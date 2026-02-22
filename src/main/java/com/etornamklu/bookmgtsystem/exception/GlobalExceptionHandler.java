package com.etornamklu.bookmgtsystem.exception;

import com.etornamklu.bookmgtsystem.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the Book Management System.
 * <p>
 * Handles common exceptions thrown by the API and maps them to
 * standardized {@link ApiResponse} objects with appropriate HTTP status codes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles cases where a requested resource is not found.
     *
     * @param ex      the {@link ResourceNotFoundException} thrown
     * @param request the current {@link WebRequest}
     * @return a {@link ResponseEntity} containing an error {@link ApiResponse} with HTTP 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(
            ResourceNotFoundException ex, WebRequest request) {

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("path", request.getDescription(false).replace("uri=", ""));

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), errorDetails));
    }

    /**
     * Handles optimistic locking failures when multiple requests attempt to update
     * the same resource concurrently.
     *
     * @param ex      the {@link ObjectOptimisticLockingFailureException} thrown
     * @param request the current {@link WebRequest}
     * @return a {@link ResponseEntity} containing an error {@link ApiResponse} with HTTP 409
     */
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiResponse<Object>> handleOptimisticLocking(
            ObjectOptimisticLockingFailureException ex, WebRequest request) {

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("path", request.getDescription(false).replace("uri=", ""));

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error("Resource was updated by another request, please retry", errorDetails));
    }

    /**
     * Handles validation errors when request bodies fail {@link jakarta.validation} constraints.
     *
     * @param ex      the {@link MethodArgumentNotValidException} thrown
     * @param request the current {@link WebRequest}
     * @return a {@link ResponseEntity} containing an error {@link ApiResponse} with HTTP 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", "Validation failed");
        errorDetails.put("errors", errors);
        errorDetails.put("path", request.getDescription(false).replace("uri=", ""));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Validation failed", errorDetails));
    }

    /**
     * Handles illegal argument exceptions thrown by business logic.
     *
     * @param ex      the {@link IllegalArgumentException} thrown
     * @param request the current {@link WebRequest}
     * @return a {@link ResponseEntity} containing an error {@link ApiResponse} with HTTP 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("path", request.getDescription(false).replace("uri=", ""));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), errorDetails));
    }

    /**
     * Handles all other uncaught exceptions globally.
     *
     * @param ex      the {@link Exception} thrown
     * @param request the current {@link WebRequest}
     * @return a {@link ResponseEntity} containing an error {@link ApiResponse} with HTTP 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(
            Exception ex, WebRequest request) {

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", "An unexpected error occurred");
        errorDetails.put("details", ex.getMessage());
        errorDetails.put("path", request.getDescription(false).replace("uri=", ""));

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Internal server error", errorDetails));
    }
}