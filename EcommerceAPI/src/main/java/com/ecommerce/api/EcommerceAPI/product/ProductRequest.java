package com.ecommerce.api.EcommerceAPI.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductRequest {
    @NotBlank(message = "Product name cannot be empty")
    private String name;

    private String description;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Stock quantity cannot be null")
    @Positive(message = "Stock quantity must be positive")
    private Integer stockQuantity;

    private String imageUrl;

    @NotNull(message = "Category ID cannot be null")
    private UUID categoryId;

    private boolean active = true;
}