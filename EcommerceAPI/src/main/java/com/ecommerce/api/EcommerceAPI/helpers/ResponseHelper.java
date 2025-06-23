package com.ecommerce.api.EcommerceAPI.helpers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHelper {
    // Success responses (unchanged)
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(ApiResponse.success(message, data));
    }

    public static <T> ResponseEntity<ApiResponse<Page<T>>> paginated(Page<T> page) {
        return ResponseEntity.ok(ApiResponse.paginatedSuccess(page));
    }

    public static ResponseEntity<ApiResponse<?>> created(Object data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resource created successfully", data));
    }

    // Enhanced error responses (work alongside GlobalExceptionHandler)
    public static ResponseEntity<ApiResponse<?>> badRequest(String message) {
        return ResponseEntity.badRequest().body(ApiResponse.error(message));
    }

    public static ResponseEntity<ApiResponse<?>> badRequest(String message, Integer errorCode) {
        return ResponseEntity.badRequest().body(ApiResponse.error(message, errorCode));
    }

    public static ResponseEntity<ApiResponse<?>> unauthorized(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(message));
    }

    public static ResponseEntity<ApiResponse<?>> unauthorized(String message, Integer errorCode) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(message, errorCode));
    }

    public static ResponseEntity<ApiResponse<?>> notFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(message));
    }

    public static ResponseEntity<ApiResponse<?>> notFound(String message, Integer errorCode) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(message, errorCode));
    }

    public static ResponseEntity<ApiResponse<?>> error(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(ApiResponse.error(message));
    }

    public static ResponseEntity<ApiResponse<?>> error(String message, Integer errorCode, HttpStatus status) {
        return ResponseEntity.status(status).body(ApiResponse.error(message, errorCode));
    }

    // For validation errors
    public static ResponseEntity<ApiResponse<?>> validationError(String message, Object errors) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(message, null, errors));
    }
}