package org.sdi.productmanager.dto;

import lombok.AllArgsConstructor;
import org.sdi.productmanager.client.UserClient;
import org.sdi.productmanager.entity.Product;
import org.sdi.productmanager.entity.Review;
import org.sdi.productmanager.entity.User;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@AllArgsConstructor
public class ReviewMapper {

    private final UserClient userClient;

    public ReviewResponse toReviewResponse(Review review) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(review.getReviewDate());

        return ReviewResponse.builder()
                .id(review.getId())
                .message(review.getMessage())
                .productId(review.getProduct().getId())
                .reviewDate(formattedDate)
                .stars(review.getStars())
                .reviewer(userClient.getUserDetails(review.getReviewer().getEmail()))
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
