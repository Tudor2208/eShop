package org.sdi.productmanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.sdi.productmanager.dto.*;
import org.sdi.productmanager.entity.Category;
import org.sdi.productmanager.entity.Product;
import org.sdi.productmanager.entity.ProductSpecifications;
import org.sdi.productmanager.exception.NotFoundException;
import org.sdi.productmanager.repository.ProductRepository;
import org.sdi.productmanager.service.CategoryService;
import org.sdi.productmanager.service.KafkaProducerService;
import org.sdi.productmanager.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;
    private final KafkaProducerService kafkaProducerService;
    private final S3Client s3Client;

    private static final String CREATE_PRODUCT_TOPIC = "product.create";
    private static final String DELETE_PRODUCT_TOPIC = "product.delete";
    private static final String BUCKET_NAME = "gccc-eshop-images";
    private static final String REGION = "eu-north-1";

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        Category category = categoryService.getCategory(request.getCategoryId());
        Product product = productMapper.toEntity(request, category);
        Product savedProduct = productRepository.save(product);

        kafkaProducerService.sendMessage(CREATE_PRODUCT_TOPIC, String.valueOf(savedProduct.getId()));
        return productMapper.toProductResponse(savedProduct);
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

        if (request.getStock() != null) {
            product.setStock(request.getStock());
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
        productRepository.findById(id).ifPresentOrElse(product -> {
                        productRepository.delete(product);
                        deleteImage(String.valueOf(product.getId()));
                        kafkaProducerService.sendMessage(DELETE_PRODUCT_TOPIC, String.valueOf(product.getId()));
                        },
                () -> {
            throw new NotFoundException(String.format("Product with id %s not found", id));
        });
    }

    @Override
    public ImageResponse uploadImage(MultipartFile file, String productId) {
        try {
            String key = productId + ".png";

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            String fileUrl = "https://" + BUCKET_NAME + ".s3." + REGION + ".amazonaws.com/" + key;

            return new ImageResponse(fileUrl);
        } catch (IOException | S3Exception e) {
            return new ImageResponse("Image upload failed: " + e.getMessage());
        }
    }

    @Override
    public String deleteImage(String productId) {
        try {
            String key = productId + ".png";

            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteRequest);

            return "Image deleted successfully from S3.";
        } catch (S3Exception e) {
            return "Failed to delete image from S3: " + e.awsErrorDetails().errorMessage();
        } catch (Exception e) {
            return "Error occurred while deleting image: " + e.getMessage();
        }
    }
}