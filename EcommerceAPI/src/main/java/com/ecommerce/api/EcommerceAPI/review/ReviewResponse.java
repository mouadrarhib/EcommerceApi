package com.ecommerce.api.EcommerceAPI.review;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ReviewResponse {
    private UUID id;
    private UUID productId;
    private String productName;
    private Integer userId;
    private String username;
    private Integer ratingValue;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}