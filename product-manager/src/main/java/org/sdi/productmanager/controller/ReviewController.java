package org.sdi.productmanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sdi.productmanager.dto.CreateReviewRequest;
import org.sdi.productmanager.dto.PaginatedResponse;
import org.sdi.productmanager.dto.ReviewResponse;
import org.sdi.productmanager.service.ReviewService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.sdi.productmanager.Constants.*;
import static org.sdi.productmanager.Constants.SLASH;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(V1 + REVIEWS)
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody CreateReviewRequest request) {
        ReviewResponse review = reviewService.createReview(request);
        return ResponseEntity.created(URI.create(V1 + REVIEWS + SLASH + review.getId())).body(review);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable(value="id") Long id) {
        return ResponseEntity.ok(reviewService.getReview(id));
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<ReviewResponse>> getReviews(@RequestParam(value="page", defaultValue="0") int page,
                                                                        @RequestParam(value="size", defaultValue="10") int size,
                                                                        @RequestParam(value="productId", required=false) Long productId,
                                                                        @RequestParam(value="sortBy", defaultValue="stars") String sortBy,
                                                                        @RequestParam(value="dir", defaultValue="asc") String dir) {
        Sort.Direction direction = dir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(reviewService.getReviews(pageable, productId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable(value="id") Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
