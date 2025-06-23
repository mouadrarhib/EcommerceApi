package com.ecommerce.api.EcommerceAPI.email;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "The new password does not match"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(304, FORBIDDEN, "Login and / or Password is incorrect"),

    // Product related errors (400 series)
    PRODUCT_NOT_FOUND(400, NOT_FOUND, "Product not found"),
    INSUFFICIENT_STOCK(401, BAD_REQUEST, "Insufficient product stock"),

    // Order related errors (410 series)
    ORDER_NOT_FOUND(410, NOT_FOUND, "Order not found"),
    ORDER_PROCESSING_ERROR(411, BAD_REQUEST, "Order processing error"),

    // Inventory related errors (420 series)
    INVENTORY_UPDATE_FAILED(420, INTERNAL_SERVER_ERROR, "Inventory update failed"),

    // Payment related errors (430 series)
    PAYMENT_FAILED(430, BAD_REQUEST, "Payment processing failed"),

    // General service errors (500 series)
    OPERATION_NOT_PERMITTED(500, FORBIDDEN, "Operation not permitted"),
    RESOURCE_NOT_FOUND(501, NOT_FOUND, "Requested resource not found"),
    INVALID_INPUT(502, BAD_REQUEST, "Invalid input data");

    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus status, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }
}