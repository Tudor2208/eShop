package org.sdi.productmanager.dto;

import lombok.Builder;
import lombok.Data;
import org.sdi.productmanager.entity.Category;

import java.util.Map;

@Builder
@Data
public class ProductResponse {

    private Long id;
    private String title;
    private Double price;
    private Category category;
    private Map<String, Object> specifications;
    private Integer stock;
    private Double rating;
    private Integer nrOfReviews;
    private String postDate;
}
