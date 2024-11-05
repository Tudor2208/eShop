package org.sdi.productmanager.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateReviewRequest {

    @NotBlank(message="Message cannot be blank")
    private String message;
    @Min(value=1, message="Rating must be at least 1 star")
    @Max(value=5, message="Rating cannot exceed 5 stars")
    @NotNull(message="Please provide a rating")
    private Integer stars;
    @NotBlank(message="The user's email cannot be blank")
    private String userEmail;
    @NotNull(message="The review must be associated with a product")
    @Positive(message="The product ID should be positive")
    private Long productId;
}
