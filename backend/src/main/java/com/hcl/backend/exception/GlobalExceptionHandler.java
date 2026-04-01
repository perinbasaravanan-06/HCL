package com.hcl.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.hcl.backend.dto.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ApiResponse apiResponse = new ApiResponse(false, ex.getMessage(), null);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequestException(BadRequestException ex, WebRequest request) {
        ApiResponse apiResponse = new ApiResponse(false, ex.getMessage(), null);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CouponInvalidException.class)
    public ResponseEntity<ApiResponse> handleCouponInvalidException(CouponInvalidException ex, WebRequest request) {
        ApiResponse apiResponse = new ApiResponse(false, ex.getMessage(), null);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ApiResponse> handleOutOfStockException(OutOfStockException ex, WebRequest request) {
        ApiResponse apiResponse = new ApiResponse(false, ex.getMessage(), null);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception ex, WebRequest request) {
        ApiResponse apiResponse = new ApiResponse(false, ex.getMessage(), null);
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
