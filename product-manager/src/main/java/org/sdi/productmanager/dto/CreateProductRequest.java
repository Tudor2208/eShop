package org.sdi.productmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Map;

@Data
public class CreateProductRequest {

    @NotBlank(message="Title cannot be empty")
    private String title;
    @NotNull(message="Price cannot be empty")
    @Positive(message = "Price must be greater than zero")
    private Double price;
    @NotNull(message="The product should have a category")
    @Positive(message="The categoryId should be positive")
    private Long categoryId;
    @NotNull(message="Specifications cannot be empty")
    private Map<String, Object> specifications;
    @Positive(message="Stock should be a positive number")
    @NotNull(message="Stock should be specified")
    private Integer stock;
}
