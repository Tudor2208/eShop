package org.sdi.productmanager.service;

import org.sdi.productmanager.dto.CreateReviewRequest;
import org.sdi.productmanager.dto.PaginatedResponse;
import org.sdi.productmanager.dto.ReviewResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public interface ReviewService {

    ReviewResponse createReview(CreateReviewRequest request);
    ReviewResponse getReview(Long id);
    PaginatedResponse<ReviewResponse> getReviews(Pageable pageable, Long productId);
    void deleteReview(Long id);
}
