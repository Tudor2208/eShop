package org.sdi.ordermanager.service;

import org.sdi.ordermanager.dto.CreateOrderRequest;
import org.sdi.ordermanager.dto.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest createOrderRequest);
    OrderResponse getOrder(Long orderId);
    List<OrderResponse> getOrdersByUser(String userId);
    void deleteOrder(Long orderId);
}
