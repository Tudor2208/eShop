package org.sdi.productmanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.sdi.productmanager.dto.*;
import org.sdi.productmanager.entity.Product;
import org.sdi.productmanager.entity.Review;
import org.sdi.productmanager.entity.User;
import org.sdi.productmanager.exception.NotFoundException;
import org.sdi.productmanager.repository.ProductRepository;
import org.sdi.productmanager.repository.ReviewRepository;
import org.sdi.productmanager.service.ReviewService;
import org.sdi.productmanager.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewResponse createReview(CreateReviewRequest request) {
        User user = userService.getUser(request.getUserEmail());
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException(String.format("Product with id %s not found", request.getProductId())));
        Review review = reviewRepository.save(reviewMapper.toEntity(request, user, product));
        return reviewMapper.toReviewResponse(review);
    }

    @Override
    public ReviewResponse getReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Review with id %d not found", id)));
        return reviewMapper.toReviewResponse(review);
    }

    @Override
    public PaginatedResponse<ReviewResponse> getReviews(Pageable pageable, Long productId) {
        Page<Review> page = null;

        if (productId != null) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NotFoundException(String.format("Product with id %s not found", productId)));
            page = reviewRepository.findAllByProduct(product, pageable);
        } else {
            page = reviewRepository.findAll(pageable);
        }

        List<ReviewResponse> reviews = page.stream()
                .map(reviewMapper::toReviewResponse)
                .toList();

        return PaginatedResponse.<ReviewResponse>builder()
                .content(reviews)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.findById(id).ifPresentOrElse(reviewRepository::delete,
                () -> {
                    throw new NotFoundException(String.format("Review with id %s not found", id));
                });
    }
}
