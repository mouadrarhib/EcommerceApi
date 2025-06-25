package com.ecommerce.api.EcommerceAPI.review;

import com.ecommerce.api.EcommerceAPI.helpers.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<?> submitReview(@RequestBody @Valid ReviewRequest request) {
        try {
            ReviewResponse response = reviewService.submitReviewAndRating(request);
            return ResponseHelper.created(response);
        } catch (IllegalArgumentException e) {
            return ResponseHelper.badRequest(e.getMessage());
        }
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable UUID reviewId,
            @RequestBody @Valid ReviewRequest request) {
        try {
            ReviewResponse response = reviewService.updateReviewAndRating(reviewId, request);
            return ResponseHelper.ok("Review updated successfully", response);
        } catch (IllegalArgumentException e) {
            return ResponseHelper.badRequest(e.getMessage());
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable UUID reviewId) {
        try {
            reviewService.deleteReviewAndRating(reviewId);
            return ResponseHelper.ok("Review deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseHelper.badRequest(e.getMessage());
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getReviewsByProductId(@PathVariable UUID productId) {
        List<ReviewResponse> responses = reviewService.getReviewsByProductId(productId);
        return ResponseHelper.ok(responses);
    }

    @GetMapping("/my-reviews")
    public ResponseEntity<?> getReviewsByCurrentUser() {
        List<ReviewResponse> responses = reviewService.getReviewsByCurrentUser();
        return ResponseHelper.ok(responses);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<?> getReviewById(@PathVariable UUID reviewId) {
        return reviewService.getReviewById(reviewId)
                .map(ResponseHelper::ok)
                .orElseGet(() -> ResponseHelper.notFoundTyped("Review not found"));
    }

    @GetMapping("/product/{productId}/average-rating")
    public ResponseEntity<?> getAverageRatingForProduct(@PathVariable UUID productId) {
        Double averageRating = reviewService.getAverageRatingForProduct(productId);
        return ResponseHelper.ok(averageRating != null ? averageRating : 0.0);
    }
}