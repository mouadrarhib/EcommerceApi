package com.ecommerce.api.EcommerceAPI.handler;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    private final BusinessErrorCodes errorCode;

    public ServiceException(String message, BusinessErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceException(BusinessErrorCodes errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }
}