package org.sdi.productmanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.sdi.productmanager.dto.*;
import org.sdi.productmanager.entity.Category;
import org.sdi.productmanager.entity.Product;
import org.sdi.productmanager.entity.ProductSpecifications;
import org.sdi.productmanager.exception.NotFoundException;
import org.sdi.productmanager.repository.ProductRepository;
import org.sdi.productmanager.service.CategoryService;
import org.sdi.productmanager.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        Category category = categoryService.getCategory(request.getCategoryId());
        Product product = productMapper.toEntity(request, category);
        return productMapper.toProductResponse(productRepository.save(product));
    }

    @Override
    public PaginatedResponse<ProductResponse> getProducts(Pageable pageable, Long categoryId, String keywords) {
        Specification<Product> spec = Specification
                .where(ProductSpecifications.hasCategory(categoryId))
                .and(ProductSpecifications.titleContains(keywords));

        Page<Product> pageableProducts = productRepository.findAll(spec, pageable);

        List<ProductResponse> products = pageableProducts.stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());

        return PaginatedResponse.<ProductResponse>builder()
                .content(products)
                .page(pageableProducts.getNumber())
                .size(pageableProducts.getSize())
                .totalElements(pageableProducts.getTotalElements())
                .totalPages(pageableProducts.getTotalPages())
                .build();
    }

    @Override
    public ProductResponse patchProduct(Long id, PatchProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Product with id %s not found", id)));

        if (request.getCategoryId() != null) {
            Category category = categoryService.getCategory(request.getCategoryId());
            product.setCategory(category);
        }

        if (request.getTitle() != null) {
            product.setTitle(request.getTitle());
        }

        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }

        if (request.getSpecifications() != null) {
            product.setSpecifications(request.getSpecifications());
        }

        return productMapper.toProductResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Product with id %s not found", id)));
        return productMapper.toProductResponse(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,
                () -> {
            throw new NotFoundException(String.format("Product with id %s not found", id));
        });
    }
}
