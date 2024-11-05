package org.sdi.productmanager.dto;

import lombok.Builder;
import lombok.Data;
import org.sdi.productmanager.entity.Product;
import org.sdi.productmanager.entity.User;

import java.util.Date;

@Data
@Builder
public class ReviewResponse {

    private Long id;
    private String message;
    private Integer stars;
    private Date reviewDate;
    private Product product;
    private User reviewer;
}
