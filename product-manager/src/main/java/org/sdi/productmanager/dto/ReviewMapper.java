package org.sdi.productmanager.dto;

import org.sdi.productmanager.entity.Product;
import org.sdi.productmanager.entity.Review;
import org.sdi.productmanager.entity.User;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ReviewMapper {

    public ReviewResponse toReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .message(review.getMessage())
                .product(review.getProduct())
                .reviewDate(review.getReviewDate())
                .stars(review.getStars())
                .reviewer(review.getReviewer())
                .build();
    }

    public Review toEntity(CreateReviewRequest request, User reviewer, Product product) {
        return Review.builder()
                .message(request.getMessage())
                .product(product)
                .reviewer(reviewer)
                .reviewDate(new Date())
                .stars(request.getStars())
                .build();
    }
}
