package org.sdi.productmanager.dto;

import org.sdi.productmanager.entity.Category;
import org.sdi.productmanager.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(CreateProductRequest request, Category category) {
        return Product.builder()
                .category(category)
                .price(request.getPrice())
                .specifications(request.getSpecifications())
                .title(request.getTitle())
                .build();
    }

    public ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .category(product.getCategory())
                .price(product.getPrice())
                .specifications(product.getSpecifications())
                .title(product.getTitle())
                .build();
    }
}
