package com.ecommerce.api.EcommerceAPI.cart;

import com.ecommerce.api.EcommerceAPI.product.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.UUID;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"cart", "product"}) // Exclude related entities from toString
@EqualsAndHashCode(exclude = {"cart", "product"}) // Exclude related entities from equals/hashCode
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonBackReference // Prevents JSON serialization loops - this side is ignored
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal subtotal;

    // Helper method to update subtotal based on product price and quantity
    public void updateSubtotal() {
        if (this.product != null && this.product.getPrice() != null && this.quantity != null) {
            this.subtotal = this.product.getPrice().multiply(BigDecimal.valueOf(this.quantity));
        } else {
            this.subtotal = BigDecimal.ZERO;
        }
    }
}