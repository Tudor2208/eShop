package org.sdi.productmanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Map;

@Data
public class PatchProductRequest {

    private String title;
    @Positive(message="Price must be greater than zero")
    private Double price;
    private Long categoryId;
    private Map<String, Object> specifications;
    @Min(value = 0, message="Stock must be greater or equal than zero")
    private Integer stock;
}
