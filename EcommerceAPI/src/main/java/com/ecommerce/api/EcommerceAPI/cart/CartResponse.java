package com.ecommerce.api.EcommerceAPI.cart;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
public class CartResponse {
    private UUID id;
    private Integer userId;
    private Set<CartItemResponse> items;
    private BigDecimal totalAmount;
}
