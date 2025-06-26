package com.ecommerce.api.EcommerceAPI.wishlist;

import com.ecommerce.api.EcommerceAPI.helpers.ResponseHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public ResponseEntity<?> getWishlist() {
        try {
            return wishlistService.getWishlist()
                    .map(ResponseHelper::ok)
                    .orElseGet(() -> ResponseHelper.notFoundTyped("Wishlist not found"));
        } catch (IllegalArgumentException e) {
            return ResponseHelper.badRequest(e.getMessage());
        }
    }

    @PostMapping("/items")
    public ResponseEntity<?> addItemToWishlist(@RequestBody WishlistItemRequest request) {
        try {
            return ResponseHelper.ok(
                    "Product added to wishlist successfully",
                    wishlistService.addItemToWishlist(request.getProductId())
            );
        } catch (IllegalArgumentException e) {
            return ResponseHelper.badRequest(e.getMessage());
        }
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<?> removeItemFromWishlist(@PathVariable UUID productId) {
        try {
            return ResponseHelper.ok(
                    "Product removed from wishlist successfully",
                    wishlistService.removeItemFromWishlist(productId)
            );
        } catch (IllegalArgumentException e) {
            return ResponseHelper.badRequest(e.getMessage());
        }
    }
}