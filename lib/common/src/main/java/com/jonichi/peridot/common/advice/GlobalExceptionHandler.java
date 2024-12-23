package com.jonichi.peridot.common.advice;

import com.jonichi.peridot.common.constant.ErrorCode;
import com.jonichi.peridot.common.dto.ApiResponse;
import com.jonichi.peridot.common.dto.ErrorResponse;
import com.jonichi.peridot.common.exception.PeridotDuplicateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global exception handler for the Envelope application.
 *
 * <p>This class handles various types of exceptions thrown throughout the application
 * and provides appropriate HTTP responses to the client. It includes handlers for validation
 * errors, custom application exceptions, and general exceptions.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles {@link MethodArgumentNotValidException} exceptions.
     *
     * <p>These exceptions occur when validation constraints on request parameters are violated.
     * The response contains a detailed map of field-specific error messages.</p>
     *
     * @param e the exception thrown due to validation errors
     * @return a {@link ResponseEntity} with a structured validation error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, List<String>>>> handleNotValidException(
            MethodArgumentNotValidException e
    ) {
        Map<String, List<String>> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            logger.error("Validation Error: {}", error.getDefaultMessage());
            String field = error.getField();
            String errorMessage = error.getDefaultMessage();

            errorMap.computeIfAbsent(field, k -> new ArrayList<>()).add(errorMessage);
        });

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiResponse<Map<String, List<String>>> response = ErrorResponse
                .<Map<String, List<String>>>builder()
                .message("Validation error")
                .code(status.value())
                .errorCode(ErrorCode.NESTED_ERROR)
                .error(errorMap)
                .build();

        return ResponseEntity.status(status).body(response);
    }

    /**
     * Handles {@link PeridotDuplicateException} exceptions.
     *
     * <p>These exceptions occur when an attempt is made to create a duplicate resource,
     * such as a user or entity that already exists.</p>
     *
     * @param e the exception thrown when a duplicate resource is detected
     * @return a {@link ResponseEntity} with a duplicate error response
     */
    @ExceptionHandler(PeridotDuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> handlePeridotDuplicateException(
            PeridotDuplicateException e
    ) {
        logger.error("Duplicate Error: {}", e.getMessage());

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiResponse<Void> response = ErrorResponse.<Void>builder()
                .code(status.value())
                .message(e.getMessage())
                .errorCode(ErrorCode.DUPLICATE)
                .build();

        return ResponseEntity.status(status).body(response);

    }

    /**
     * Handles {@link DataIntegrityViolationException} exceptions, typically caused by
     * database integrity constraints such as unique key violations.
     *
     * <p>Extracts the duplicate field from the exception message and constructs a
     * standardized API error response with a BAD_REQUEST status and a descriptive error
     * message.</p>
     *
     * @param e the {@code DataIntegrityViolationException} to handle
     * @return a {@link ResponseEntity} containing the error response and the HTTP status
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(
            DataIntegrityViolationException e
    ) {
        logger.error("Data Integrity Error: {}", e.getMessage());

        String message = e.getMessage();
        String duplicateField = message.substring(
                message.indexOf("(") + 1,
                message.indexOf(")", message.indexOf("(") + 1));

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiResponse<Void> response = ErrorResponse.<Void>builder()
                .code(status.value())
                .message(duplicateField + " already exists")
                .errorCode(ErrorCode.DUPLICATE)
                .build();

        return ResponseEntity.status(status).body(response);
    }

    /**
     * Handles {@link NoResourceFoundException} and maps it to a 404 Not Found response.
     *
     * <p>This method constructs an error response using {@link ErrorResponse} with a specific
     * error code ({@link ErrorCode#NOT_FOUND}). It ensures that clients receive a structured
     * error response with meaningful details when a resource cannot be found.</p>
     *
     * @param e the {@link NoResourceFoundException} thrown when a requested resource is not found
     * @return a {@link ResponseEntity} containing a structured error response
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(
            NoResourceFoundException e
    ) {
        logger.error("No Resource Error: {}", e.getMessage());

        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiResponse<Void> response = ErrorResponse.<Void>builder()
                .code(status.value())
                .message(e.getMessage())
                .errorCode(ErrorCode.NOT_FOUND)
                .build();

        return ResponseEntity.status(status).body(response);
    }

    /**
     * Handles {@link BadCredentialsException} thrown during authentication.
     *
     * <p>This method intercepts {@link BadCredentialsException} and returns a response indicating
     * that the provided username or password is invalid. </p>
     *
     * @param e the {@link BadCredentialsException} thrown during authentication
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with an error message and
     *     status code 401 (UNAUTHORIZED)
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException e) {

        logger.error("Bad Credentials Error: {}", e.getMessage());

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ApiResponse<Void> response = ErrorResponse.<Void>builder()
                .code(status.value())
                .message(e.getMessage())
                .errorCode(ErrorCode.UNAUTHORIZED)
                .build();

        return ResponseEntity.status(status).body(response);

    }

    /**
     * Handles all uncaught exceptions in the application.
     *
     * <p>This is a fallback handler that catches any exceptions not explicitly handled by other
     * methods. It returns a generic "Internal Server Error" response.</p>
     *
     * @param e the exception that was not explicitly handled
     * @return a {@link ResponseEntity} with an internal server error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAll(Exception e) {
        logger.error("Exception occurred", e);


        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiResponse<Void> response = ErrorResponse.<Void>builder()
                .code(status.value())
                .message("Internal Server Error")
                .errorCode(ErrorCode.INTERNAL_SERVER_ERROR)
                .build();

        return ResponseEntity.status(status).body(response);
    }
}
