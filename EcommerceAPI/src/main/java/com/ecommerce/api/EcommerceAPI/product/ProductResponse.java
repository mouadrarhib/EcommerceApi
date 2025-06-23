package com.ecommerce.api.EcommerceAPI.product;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String imageUrl;
    private UUID categoryId;
    private String categoryName;
    private boolean active;
}