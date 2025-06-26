package com.ecommerce.api.EcommerceAPI.wishlist;


import com.ecommerce.api.EcommerceAPI.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {

    @EntityGraph(attributePaths = {"wishlistItems", "wishlistItems.product"})
    @Query("SELECT w FROM Wishlist w WHERE w.user.id = :userId")
    Optional<Wishlist> findByUserIdWithItems(Integer userId);

    Optional<Wishlist> findByUser(User user);
}
