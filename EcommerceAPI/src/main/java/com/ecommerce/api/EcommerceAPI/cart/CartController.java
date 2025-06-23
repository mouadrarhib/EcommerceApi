package com.ecommerce.api.EcommerceAPI.cart;

import com.ecommerce.api.EcommerceAPI.helpers.ApiResponse;
import com.ecommerce.api.EcommerceAPI.helpers.AuthHelper;
import com.ecommerce.api.EcommerceAPI.helpers.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Helper method to get current authenticated user ID
    // This method now uses AuthHelper to retrieve the current user's ID.
    private Integer getCurrentUserId() {
        Integer userId = AuthHelper.getCurrentAuthenticatedUserId();
        if (userId == null) {
            // In a real application, you might throw an AuthenticationException
            // or return an appropriate error response if the user is not authenticated.
            throw new IllegalStateException("User not authenticated.");
        }
        return userId;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCartForCurrentUser() {
        try {
            Integer userId = getCurrentUserId();
            return cartService.getCartByUserId(userId)
                    .map(cart -> ResponseHelper.ok(convertToCartResponse(cart)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.<CartResponse>builder()
                                    .success(false)
                                    .message("Cart not found for current user")
                                    .build()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<CartResponse>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartResponse>> addItemToCart(@Valid @RequestBody CartItemRequest cartItemRequest) {
        try {
            Integer userId = getCurrentUserId();
            Cart updatedCart = cartService.addItemToCart(userId, cartItemRequest.getProductId(), cartItemRequest.getQuantity());
            return ResponseHelper.ok("Product added to cart successfully", convertToCartResponse(updatedCart));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<CartResponse>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<CartResponse>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<CartResponse>> updateCartItemQuantity(@Valid @RequestBody CartItemRequest cartItemRequest) {
        try {
            Integer userId = getCurrentUserId();
            Cart updatedCart = cartService.updateCartItemQuantity(userId, cartItemRequest.getProductId(), cartItemRequest.getQuantity());
            return ResponseHelper.ok("Cart item quantity updated successfully", convertToCartResponse(updatedCart));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<CartResponse>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<CartResponse>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResponse<CartResponse>> removeItemFromCart(@PathVariable UUID productId) {
        try {
            // Fixed: Keep userId as Integer, don't cast to UUID
            Integer userId = getCurrentUserId();
            Cart updatedCart = cartService.removeItemFromCart(userId, productId);
            return ResponseHelper.ok("Product removed from cart successfully", convertToCartResponse(updatedCart));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<CartResponse>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<CartResponse>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<CartResponse>> clearCart() {
        try {
            Integer userId = getCurrentUserId();
            Cart clearedCart = cartService.clearCart(userId);
            return ResponseHelper.ok("Cart cleared successfully", convertToCartResponse(clearedCart));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<CartResponse>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<CartResponse>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }

    // Helper method to convert Cart entity to CartResponse DTO
    private CartResponse convertToCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setUserId(cart.getUser() != null ? cart.getUser().getId() : null);
        response.setTotalAmount(cart.getTotalAmount());

        Set<CartItemResponse> itemResponses = cart.getCartItems().stream()
                .map(this::convertToCartItemResponse)
                .collect(Collectors.toSet());
        response.setItems(itemResponses);
        return response;
    }

    // Helper method to convert CartItem entity to CartItemResponse DTO
    private CartItemResponse convertToCartItemResponse(CartItem cartItem) {
        CartItemResponse response = new CartItemResponse();
        response.setId(cartItem.getId());
        response.setProductId(cartItem.getProduct().getId());
        response.setProductName(cartItem.getProduct().getName());
        response.setProductImageUrl(cartItem.getProduct().getImageUrl());
        response.setProductPrice(cartItem.getProduct().getPrice());
        response.setQuantity(cartItem.getQuantity());
        response.setSubtotal(cartItem.getSubtotal());
        return response;
    }
}