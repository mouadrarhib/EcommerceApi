package com.ecommerce.api.EcommerceAPI.review;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ReviewRequest {
    @NotNull(message = "Product ID cannot be null")
    private UUID productId;

    @Min(value = 1, message = "Rating value must be at least 1")
    @Max(value = 5, message = "Rating value must be at most 5")
    private Integer ratingValue;

    private String comment;
}
