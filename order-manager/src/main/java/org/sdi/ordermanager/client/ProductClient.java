package org.sdi.ordermanager.client;

import lombok.RequiredArgsConstructor;
import org.sdi.ordermanager.config.ClientProperties;
import org.sdi.ordermanager.dto.PatchProductRequest;
import org.sdi.ordermanager.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ProductClient {

    private final ClientProperties clientProperties;
    private static final String GET_PRODUCTS_PATH = "/api/v1/products/{productId}";

    @Autowired
    private RestTemplate restTemplate;

    @Cacheable(value = "productCache")
    public ProductResponse getProduct(Long id) {
        try {
            final String url = buildUrl(id);
            return restTemplate.getForObject(url, ProductResponse.class);
        } catch(RestClientException ex) {
            throw new RuntimeException(String.format("Error while getting product with id=%d from product-manager", id), ex);
        }
    }

    @CachePut(value = "productCache", key = "#id")
    public ProductResponse updateProduct(Long id, PatchProductRequest productRequest) {
        try {
            final String url = buildUrl(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PatchProductRequest> entity = new HttpEntity<>(productRequest, headers);
            ResponseEntity<ProductResponse> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    ProductResponse.class
            );

            return responseEntity.getBody();
        } catch (RestClientException ex) {
            throw new RuntimeException(String.format("Error while updating product with id=%d from product-manager", id), ex);
        }
    }

    private String buildUrl(Long id) {
        String baseUrl = clientProperties.getProductManager().getBaseUrl();
        return baseUrl + GET_PRODUCTS_PATH.replace("{productId}", String.valueOf(id));
    }
}
