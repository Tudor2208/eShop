package org.sdi.productmanager.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ReviewResponse {

    private Long id;
    private String message;
    private Integer stars;
    private String reviewDate;
    private Long productId;
    private UserDetails reviewer;
}
