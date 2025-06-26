package com.ecommerce.api.EcommerceAPI.mappers;

import com.ecommerce.api.EcommerceAPI.product.Product;
import com.ecommerce.api.EcommerceAPI.wishlist.Wishlist;
import com.ecommerce.api.EcommerceAPI.wishlist.WishlistItem;
import com.ecommerce.api.EcommerceAPI.wishlist.WishlistItemResponse;
import com.ecommerce.api.EcommerceAPI.wishlist.WishlistResponse;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class WishlistMapper {

    public WishlistResponse toResponse(Wishlist wishlist) {
        if (wishlist == null) {
            return null;
        }

        WishlistResponse response = new WishlistResponse();
        response.setId(wishlist.getId());

        if (wishlist.getUser() != null) {
            response.setUserId(wishlist.getUser().getId());
        }

        response.setCreatedAt(wishlist.getCreatedAt());
        response.setUpdatedAt(wishlist.getUpdatedAt());
        return response;
    }

    public WishlistItemResponse toItemResponse(WishlistItem item) {
        if (item == null) {
            return null;
        }

        WishlistItemResponse response = new WishlistItemResponse();
        response.setId(item.getId());
        response.setAddedAt(item.getAddedAt());

        Product product = item.getProduct();
        if (product != null) {
            response.setProductId(product.getId());
            response.setProductName(product.getName());
            response.setProductImageUrl(product.getImageUrl());
        }

        return response;
    }
}