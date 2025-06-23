package com.ecommerce.api.EcommerceAPI.product;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank(message = "Category name cannot be empty")
    private String name;

    private String description;
}