package org.sdi.productmanager.service;

import org.sdi.productmanager.dto.CreateProductRequest;
import org.sdi.productmanager.dto.PaginatedResponse;
import org.sdi.productmanager.dto.PatchProductRequest;
import org.sdi.productmanager.dto.ProductResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Component
public interface ProductService {
    ProductResponse createProduct(CreateProductRequest request);
    PaginatedResponse<ProductResponse> getProducts(Pageable pageable, Long categoryId, String keywords);
    ProductResponse patchProduct(Long id, PatchProductRequest request);
    ProductResponse getProduct(Long id);
    void deleteProduct(Long id);
}
