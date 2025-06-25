package com.ecommerce.api.EcommerceAPI.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByProductId(UUID productId);
    Optional<Review> findByProductIdAndUserId(UUID productId, Integer userId);
    List<Review> findByUserId(Integer userId);
}
