package org.sdi.productmanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sdi.productmanager.dto.CreateProductRequest;
import org.sdi.productmanager.dto.PaginatedResponse;
import org.sdi.productmanager.dto.PatchProductRequest;
import org.sdi.productmanager.dto.ProductResponse;
import org.sdi.productmanager.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.sdi.productmanager.Constants.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(V1 + PRODUCTS)
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductResponse product = productService.createProduct(request);
        return ResponseEntity.created(URI.create(V1 + PRODUCTS + SLASH + product.getId())).body(product);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<ProductResponse>> getProducts(@RequestParam(value="page", defaultValue="0") int page,
                                                                          @RequestParam(value="size", defaultValue="10") int size,
                                                                          @RequestParam(value="categoryId", required = false) Long categoryId,
                                                                          @RequestParam(value="keywords", required = false) String keywords,
                                                                          @RequestParam(value="sortBy", defaultValue="price") String sortBy,
                                                                          @RequestParam(value="sortOrder", defaultValue="asc") String sortOrder) {
        Sort.Direction direction = sortOrder.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(productService.getProducts(pageable, categoryId, keywords));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable(value="id") Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> patchProduct(@PathVariable(value="id") Long id,
                                                        @Valid @RequestBody PatchProductRequest request) {
        return ResponseEntity.ok(productService.patchProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(value="id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
