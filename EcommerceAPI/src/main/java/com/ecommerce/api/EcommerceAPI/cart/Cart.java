package com.ecommerce.api.EcommerceAPI.cart;

import com.ecommerce.api.EcommerceAPI.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.UUID;
import java.util.Set;
import java.util.HashSet;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"cartItems", "user"}) // Exclude to prevent recursion and lazy loading issues
@EqualsAndHashCode(exclude = {"cartItems", "user"}) // Exclude from equals/hashCode
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // Prevents JSON serialization loops
    private Set<CartItem> cartItems = new HashSet<>();

    @Column(nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    // Constructor for creating a cart for a user
    public Cart(User user) {
        this.user = user;
        this.totalAmount = BigDecimal.ZERO;
        this.cartItems = new HashSet<>();
    }

    // Helper method to update total amount
    public void updateTotalPrice() {
        this.totalAmount = this.cartItems.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}