package org.sdi.ordermanager.service;

import org.springframework.stereotype.Component;

@Component
public interface ProductService {
    void createProduct(String id);
    void deleteProduct(String id);
}
