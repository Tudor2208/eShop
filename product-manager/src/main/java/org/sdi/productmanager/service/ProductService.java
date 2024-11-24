package org.sdi.productmanager.service;

import org.sdi.productmanager.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
public interface ProductService {
    ProductResponse createProduct(CreateProductRequest request);
    PaginatedResponse<ProductResponse> getProducts(Pageable pageable, Long categoryId, String keywords);
    ProductResponse patchProduct(Long id, PatchProductRequest request);
    ProductResponse getProduct(Long id);
    void deleteProduct(Long id);
    ImageResponse uploadImage(MultipartFile file, String productId);
    String deleteImage(String productId);
}
