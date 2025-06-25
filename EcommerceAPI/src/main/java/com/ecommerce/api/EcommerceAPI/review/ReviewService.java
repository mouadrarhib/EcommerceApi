package com.ecommerce.api.EcommerceAPI.review;

import com.ecommerce.api.EcommerceAPI.helpers.AuthHelper;
import com.ecommerce.api.EcommerceAPI.product.Product;
import com.ecommerce.api.EcommerceAPI.product.ProductService;
import com.ecommerce.api.EcommerceAPI.user.User;
import com.ecommerce.api.EcommerceAPI.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RatingRepository ratingRepository;
    private final ProductService productService;

    public ReviewService(ReviewRepository reviewRepository, RatingRepository ratingRepository,
                         UserRepository userRepository, ProductService productService) {
        this.reviewRepository = reviewRepository;
        this.ratingRepository = ratingRepository;
        this.productService = productService;
    }

    @Transactional
    public ReviewResponse submitReviewAndRating(ReviewRequest request) {
        User user = getAuthenticatedUser();
        Product product = productService.getProductById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + request.getProductId()));

        Optional<Review> existingReview = reviewRepository.findByProductIdAndUserId(request.getProductId(), user.getId());
        if (existingReview.isPresent()) {
            throw new IllegalArgumentException("User has already submitted a review for this product.");
        }

        Review review = new Review(product, user, request.getComment());
        reviewRepository.save(review);

        Rating rating = new Rating(product, user, request.getRatingValue());
        ratingRepository.save(rating);

        return convertToResponse(review, request.getRatingValue());
    }

    @Transactional
    public ReviewResponse updateReviewAndRating(UUID reviewId, ReviewRequest request) {
        User user = getAuthenticatedUser();
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User is not authorized to update this review.");
        }

        review.setComment(request.getComment());
        reviewRepository.save(review);

        Rating rating = ratingRepository.findByProductIdAndUserId(review.getProduct().getId(), user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Rating not found for this review."));
        rating.setRatingValue(request.getRatingValue());
        ratingRepository.save(rating);

        return convertToResponse(review, request.getRatingValue());
    }

    @Transactional
    public void deleteReviewAndRating(UUID reviewId) {
        User user = getAuthenticatedUser();
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User is not authorized to delete this review.");
        }

        ratingRepository.findByProductIdAndUserId(review.getProduct().getId(), user.getId())
                .ifPresent(ratingRepository::delete);

        reviewRepository.delete(review);
    }

    public List<ReviewResponse> getReviewsByProductId(UUID productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(review -> {
                    Integer rating = ratingRepository.findByProductIdAndUserId(review.getProduct().getId(), review.getUser().getId())
                            .map(Rating::getRatingValue)
                            .orElse(null);
                    return convertToResponse(review, rating);
                })
                .toList();
    }

    public List<ReviewResponse> getReviewsByCurrentUser() {
        User user = getAuthenticatedUser();
        return reviewRepository.findByUserId(user.getId()).stream()
                .map(review -> {
                    Integer rating = ratingRepository.findByProductIdAndUserId(review.getProduct().getId(), user.getId())
                            .map(Rating::getRatingValue)
                            .orElse(null);
                    return convertToResponse(review, rating);
                })
                .toList();
    }

    public Optional<ReviewResponse> getReviewById(UUID reviewId) {
        return reviewRepository.findById(reviewId)
                .map(review -> {
                    Integer rating = ratingRepository.findByProductIdAndUserId(review.getProduct().getId(), review.getUser().getId())
                            .map(Rating::getRatingValue)
                            .orElse(null);
                    return convertToResponse(review, rating);
                });
    }

    public Double getAverageRatingForProduct(UUID productId) {
        return ratingRepository.findAverageRatingByProductId(productId);
    }

    private User getAuthenticatedUser() {
        User user = AuthHelper.getCurrentAuthenticatedUser();
        if (user == null) {
            throw new IllegalArgumentException("No authenticated user found.");
        }
        return user;
    }

    private ReviewResponse convertToResponse(Review review, Integer ratingValue) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setProductId(review.getProduct().getId());
        response.setProductName(review.getProduct().getName());
        response.setUserId(review.getUser().getId());
        response.setUsername(review.getUser().getUsername());
        response.setRatingValue(ratingValue);
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());
        response.setUpdatedAt(review.getUpdatedAt());
        return response;
    }
}