package com.ecommerce.api.EcommerceAPI.wishlist;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class WishlistItemResponse {
    private UUID id;
    private UUID productId;
    private String productName;
    private String productImageUrl;
    private LocalDateTime addedAt;
}