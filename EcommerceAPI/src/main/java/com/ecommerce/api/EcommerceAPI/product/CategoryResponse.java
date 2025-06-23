package com.ecommerce.api.EcommerceAPI.product;

import lombok.Data;
import java.util.UUID;

@Data
public class CategoryResponse {
    private UUID id;
    private String name;
    private String description;
}