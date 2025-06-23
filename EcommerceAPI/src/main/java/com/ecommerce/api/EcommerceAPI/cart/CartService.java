package com.ecommerce.api.EcommerceAPI.cart;

import com.ecommerce.api.EcommerceAPI.product.Product;
import com.ecommerce.api.EcommerceAPI.product.ProductService;
import com.ecommerce.api.EcommerceAPI.user.User;
import com.ecommerce.api.EcommerceAPI.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final UserRepository userRepository; // To get the current authenticated user

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductService productService, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.userRepository = userRepository;
    }

    // Helper method to get or create a cart for the current user
    @Transactional
    public Cart getOrCreateCartForCurrentUser() {
        // In a real application, you would get the authenticated user's ID from Spring Security context
        // For now, let's assume a way to get the current user (e.g., from a token or session)
        // This is a placeholder. You need to implement how to get the current authenticated user.
        // Example: User currentUser = userService.getCurrentAuthenticatedUser();
        // For demonstration, let's assume a user with a fixed ID or fetch dynamically.
        // For a real application, you'd integrate with Spring Security context.
        // For this guide, we'll assume a user is passed or retrieved.

        // IMPORTANT: Replace this with actual user retrieval from security context
        // For example:
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // String username = authentication.getName();
        // User currentUser = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        // For now, let's use a dummy user or assume it's passed from controller for simplicity
        // This method should ideally be called internally after user authentication.
        throw new UnsupportedOperationException("User retrieval from security context not implemented. Implement userService.getCurrentAuthenticatedUser() or pass user to this service.");
    }

    @Transactional
    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));
    }

    @Transactional
    public Cart addItemToCart(Integer userId, UUID productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        Cart cart = getOrCreateCart(user);
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        if (product.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for product: " + product.getName() + ". Available: " + product.getStockQuantity());
        }

        Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProduct(cart, product);

        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cart.getCartItems().add(cartItem);
        }
        cartItem.updateSubtotal();
        cartItemRepository.save(cartItem);
        cart.updateTotalPrice();
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateCartItemQuantity(Integer userId, UUID productId, int newQuantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user with id: " + userId));
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        if (newQuantity <= 0) {
            return removeItemFromCart(userId, productId); // Remove item if quantity is 0 or less
        }
        if (product.getStockQuantity() < newQuantity) {
            throw new IllegalArgumentException("Insufficient stock for product: " + product.getName() + ". Available: " + product.getStockQuantity());
        }

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new IllegalArgumentException("Product not found in cart."));

        cartItem.setQuantity(newQuantity);
        cartItem.updateSubtotal();
        cartItemRepository.save(cartItem);
        cart.updateTotalPrice();
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeItemFromCart(Integer userId, UUID productId) {
        User user = (User) userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user with id: " + userId));
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new IllegalArgumentException("Product not found in cart."));

        cart.getCartItems().remove(cartItem); // This will trigger orphanRemoval
        cartItemRepository.delete(cartItem);
        cart.updateTotalPrice();
        return cartRepository.save(cart);
    }

    public Optional<Cart> getCartByUserId(Integer userId) {
        return cartRepository.findByUserId(userId);
    }

    @Transactional
    public Cart clearCart(Integer userId) {
        User user = (User) userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user with id: " + userId));

        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cart.updateTotalPrice(); // Should set total to zero
        return cartRepository.save(cart);
    }

    // You might also need a method to merge carts if you support anonymous shopping
    // For example: mergeAnonymousCartIntoUserCart(String anonymousCartId, UUID userId)
}