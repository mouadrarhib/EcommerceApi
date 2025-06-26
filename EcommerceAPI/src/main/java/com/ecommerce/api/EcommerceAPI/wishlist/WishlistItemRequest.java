package com.ecommerce.api.EcommerceAPI.wishlist;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class WishlistItemRequest {
    @NotNull(message = "Product ID cannot be null")
    private UUID productId;
}