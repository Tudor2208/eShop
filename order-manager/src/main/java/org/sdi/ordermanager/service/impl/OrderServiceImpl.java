package org.sdi.ordermanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.sdi.ordermanager.client.ProductClient;
import org.sdi.ordermanager.dto.*;
import org.sdi.ordermanager.entity.Order;
import org.sdi.ordermanager.entity.OrderItem;
import org.sdi.ordermanager.entity.Product;
import org.sdi.ordermanager.entity.User;
import org.sdi.ordermanager.exception.NotFoundException;
import org.sdi.ordermanager.repository.OrderRepository;
import org.sdi.ordermanager.repository.ProductRepository;
import org.sdi.ordermanager.repository.UserRepository;
import org.sdi.ordermanager.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductClient productClient;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse createOrder(CreateOrderRequest createOrderRequest) {
        User user = userRepository.findById(createOrderRequest.getUserEmail())
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", createOrderRequest.getUserEmail())));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());

        List<OrderItem> orderItems = new ArrayList<>();

        for (CreateOrderRequest.OrderItemRequest itemRequest : createOrderRequest.getOrderItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new NotFoundException(String.format("Product with id %s not found", itemRequest.getProductId())));

            ProductResponse productResponse = productClient.getProduct(product.getId());

            OrderItem orderItem = new OrderItem();
            orderItem.setProductTitle(productResponse.getTitle());
            orderItem.setProductPrice(productResponse.getPrice());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        for (CreateOrderRequest.OrderItemRequest itemRequest : createOrderRequest.getOrderItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new NotFoundException(String.format("Product with id %s not found", itemRequest.getProductId())));
            ProductResponse productResponse = productClient.getProduct(product.getId());

            int newStock = productResponse.getStock() - itemRequest.getQuantity();
            productClient.updateProduct(product.getId(), new PatchProductRequest(newStock));
        }

        return orderMapper.toDTO(savedOrder);
    }


    @Override
    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format("Order with id %s not found", orderId)));
        return orderMapper.toDTO(order);
    }

    @Override
    public List<OrderResponse> getOrdersByUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found", userId)));

        List<Order> allByUserId = orderRepository.findAllByUserEmail(userId);
        return allByUserId.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }
}
