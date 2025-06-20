package com.ecommerce.api.EcommerceAPI.helpers;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private Integer errorCode;  // Add this to align with your ExceptionResponse
    private PaginationInfo pagination;

    @Data
    @Builder
    public static class PaginationInfo {
        private int currentPage;
        private int totalPages;
        private long totalItems;
        private int itemsPerPage;
    }

    // Success methods (unchanged)
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("Operation successful")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    // Paginated success (unchanged)
    public static <T> ApiResponse<Page<T>> paginatedSuccess(Page<T> page) {
        return ApiResponse.<Page<T>>builder()
                .success(true)
                .message("Data retrieved successfully")
                .data(page)
                .pagination(PaginationInfo.builder()
                        .currentPage(page.getNumber())
                        .totalPages(page.getTotalPages())
                        .totalItems(page.getTotalElements())
                        .itemsPerPage(page.getSize())
                        .build())
                .build();
    }

    // Enhanced error methods (add error code support)
    public static ApiResponse<?> error(String message) {
        return error(message, null);
    }

    public static ApiResponse<?> error(String message, Integer errorCode) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .build();
    }

    public static ApiResponse<?> error(String message, Integer errorCode, Object data) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .data(data)
                .build();
    }
}