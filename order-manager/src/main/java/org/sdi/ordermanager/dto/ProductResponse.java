package org.sdi.ordermanager.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductResponse {
    private String title;
    private Double price;
    private Integer stock;
}
