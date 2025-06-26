package com.ecommerce.api.EcommerceAPI.wishlist;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class WishlistResponse {
    private UUID id;
    private Integer userId;
    private Set<WishlistItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}