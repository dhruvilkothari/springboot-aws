package com.example.CrudApp.demo.advice;

import com.example.CrudApp.demo.response.ApiResponse;
import com.example.CrudApp.demo.exeception.ResourceNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(ResourceNotFoundException ex) {
        ApiResponse<Object> response = new ApiResponse<>(false, ex.getMessage(), null, "NOT_FOUND");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Handle DataIntegrityViolationException (Spring wrapper)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
        String message = "Duplicate entry detected";

        if (ex.getMostSpecificCause() != null && ex.getMostSpecificCause().getMessage().contains("user_record")) {
            message = "Email already exists";
        }

        ApiResponse<Object> response = new ApiResponse<>(false, message, null, "DUPLICATE_ENTRY");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle Hibernate ConstraintViolationException (in case not wrapped)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        String message = "Duplicate entry detected";
        if (ex.getConstraintName() != null && ex.getConstraintName().contains("user_record")) {
            message = "Email already exists";
        }
        ApiResponse<Object> response = new ApiResponse<>(false, message, null, "DUPLICATE_ENTRY");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle TransactionSystemException (outer wrapper sometimes)
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ApiResponse<Object>> handleTransactionException(TransactionSystemException ex) {
        Throwable cause = ex.getRootCause();
        if (cause instanceof ConstraintViolationException || cause instanceof DataIntegrityViolationException) {
            return handleDataIntegrity((DataIntegrityViolationException) cause);
        }
        ApiResponse<Object> response = new ApiResponse<>(false, "Transaction failed", null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> randomError(Exception ex) {
        ex.printStackTrace(); // helpful for debugging
        ApiResponse<Object> response = new ApiResponse<>(false, "Something went wrong", null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // Generic fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(Exception ex) {
        ex.printStackTrace(); // helpful for debugging
        ApiResponse<Object> response = new ApiResponse<>(false, "Something went wrong", null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}