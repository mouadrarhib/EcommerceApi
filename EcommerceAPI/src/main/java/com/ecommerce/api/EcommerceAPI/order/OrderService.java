package com.ecommerce.api.EcommerceAPI.order;

import com.ecommerce.api.EcommerceAPI.cart.Cart;
import com.ecommerce.api.EcommerceAPI.cart.CartItem;
import com.ecommerce.api.EcommerceAPI.cart.CartService;
import com.ecommerce.api.EcommerceAPI.helpers.AuthHelper;
import com.ecommerce.api.EcommerceAPI.product.Product;
import com.ecommerce.api.EcommerceAPI.product.ProductService;
import com.ecommerce.api.EcommerceAPI.user.User;
import com.ecommerce.api.EcommerceAPI.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        UserRepository userRepository, CartService cartService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.productService = productService;
    }

    @Transactional
    public Order placeOrder() {
        // Get the currently authenticated user
        User authenticatedUser = AuthHelper.getCurrentAuthenticatedUser();
        if (authenticatedUser == null) {
            throw new SecurityException("User must be authenticated to place an order");
        }

        Integer userId = authenticatedUser.getId();

        Cart cart = cartService.getCartByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for authenticated user"));

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot place an order with an empty cart.");
        }

        // Create the Order first
        Order order = new Order();
        order.setUser(authenticatedUser);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderItems(new HashSet<>()); // Initialize the set

        // Check product stock and prepare order items
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            Integer requestedQuantity = cartItem.getQuantity();

            if (product.getStockQuantity() < requestedQuantity) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName() +
                        ". Available: " + product.getStockQuantity() + ", Requested: " + requestedQuantity);
            }

            // Create OrderItem and set the relationship
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order); // This is critical!
            orderItem.setProduct(product);
            orderItem.setQuantity(requestedQuantity);
            orderItem.setPriceAtPurchase(product.getPrice());
            orderItem.updateSubtotal();

            // Add to order's items
            order.getOrderItems().add(orderItem);

            // Decrease product stock
            product.setStockQuantity(product.getStockQuantity() - requestedQuantity);
            productService.updateProduct(product.getId(), product, null);
        }

        // Calculate total amount
        order.updateTotalPrice();

        // Save the order (which will cascade to order items)
        Order savedOrder = orderRepository.save(order);

        // Clear the user's cart after placing the order
        cartService.clearCart(userId);

        return savedOrder;
    }

    public Optional<Order> getOrderById(UUID orderId) {
        // Check if user is authenticated
        if (!AuthHelper.isAuthenticated()) {
            throw new SecurityException("User must be authenticated to view orders");
        }

        User authenticatedUser = AuthHelper.getCurrentAuthenticatedUser();

        // If admin, allow access to any order
        if (AuthHelper.isAdmin()) {
            return orderRepository.findById(orderId);
        }

        // If regular user, only allow access to their own orders
        return orderRepository.findByIdAndUser(orderId, authenticatedUser);
    }

    public Optional<Order> getOrderByIdAndUser(UUID orderId) {
        // Get the currently authenticated user
        User authenticatedUser = AuthHelper.getCurrentAuthenticatedUser();
        if (authenticatedUser == null) {
            throw new SecurityException("User must be authenticated to view orders");
        }

        return orderRepository.findByIdAndUser(orderId, authenticatedUser);
    }

    public List<Order> getOrdersByCurrentUser() {
        // Get the currently authenticated user
        User authenticatedUser = AuthHelper.getCurrentAuthenticatedUser();
        if (authenticatedUser == null) {
            throw new SecurityException("User must be authenticated to view orders");
        }

        return orderRepository.findByUserOrderByOrderDateDesc(authenticatedUser);
    }

    // Admin-only method to get orders by any user ID
    public List<Order> getOrdersByUserId(Integer userId) {
        // Check if user is admin
        if (!AuthHelper.isAdmin()) {
            throw new SecurityException("Only administrators can view orders for other users");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    @Transactional
    public Order updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        // Only admins can update order status
        if (!AuthHelper.isAdmin()) {
            throw new SecurityException("Only administrators can update order status");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        // Implement state transition logic if needed
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(UUID orderId) {
        // Get the currently authenticated user
        User authenticatedUser = AuthHelper.getCurrentAuthenticatedUser();
        if (authenticatedUser == null) {
            throw new SecurityException("User must be authenticated to cancel orders");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        // Check if user owns the order or is admin
        if (!AuthHelper.isAdmin() && !order.getUser().getId().equals(authenticatedUser.getId())) {
            throw new SecurityException("You can only cancel your own orders");
        }

        if (order.getStatus() == OrderStatus.CANCELLED ||
                order.getStatus() == OrderStatus.DELIVERED ||
                order.getStatus() == OrderStatus.REFUNDED) {
            throw new IllegalStateException("Order cannot be cancelled in its current status: " + order.getStatus());
        }

        // Rollback product stock
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productService.updateProduct(product.getId(), product, null);
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    // Admin-only method to get all orders
    public List<Order> getAllOrders() {
        if (!AuthHelper.isAdmin()) {
            throw new SecurityException("Only administrators can view all orders");
        }
        return orderRepository.findAll();
    }

    // Helper method to validate user access to an order
    private void validateUserAccessToOrder(Order order, User user) {
        if (!AuthHelper.isAdmin() && !order.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Access denied: You can only access your own orders");
        }
    }
}