package com.ecommerce.api.EcommerceAPI.order;


import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class OrderRequest {
    // For now, we assume the order is placed from the current user's cart.
    // Future enhancements might include shipping address, payment method details, etc.
    // Example: @NotBlank(message = "Shipping address cannot be blank")
    // private String shippingAddress;
}