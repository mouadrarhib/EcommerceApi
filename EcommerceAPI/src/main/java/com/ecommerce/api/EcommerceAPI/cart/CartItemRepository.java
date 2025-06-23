package com.ecommerce.api.EcommerceAPI.cart;

import com.ecommerce.api.EcommerceAPI.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    Set<CartItem> findByCart(Cart cart);
}
