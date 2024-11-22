package org.sdi.ordermanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.sdi.ordermanager.entity.Product;
import org.sdi.ordermanager.repository.ProductRepository;
import org.sdi.ordermanager.service.ProductService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private static final String CREATE_PRODUCT_TOPIC = "product.create";
    private static final String DELETE_PRODUCT_TOPIC = "product.delete";

    @Override
    @KafkaListener(topics = CREATE_PRODUCT_TOPIC)
    public void createProduct(String id) {
        Product product = Product.builder().id(Long.parseLong(id)).build();
        productRepository.save(product);
    }

    @Override
    @KafkaListener(topics = DELETE_PRODUCT_TOPIC)
    public void deleteProduct(String id) {
        productRepository.findById(Long.parseLong(id)).ifPresent(productRepository::delete);
    }
}
