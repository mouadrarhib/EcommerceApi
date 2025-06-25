package com.ecommerce.api.EcommerceAPI.review;

import com.ecommerce.api.EcommerceAPI.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {
    Optional<Rating> findByProductIdAndUserId(UUID productId, Integer userId);
    List<Rating> findByProductId(UUID productId);

    @Query("SELECT AVG(r.ratingValue) FROM Rating r WHERE r.product.id = :productId")
    Double findAverageRatingByProductId(UUID productId);
}
