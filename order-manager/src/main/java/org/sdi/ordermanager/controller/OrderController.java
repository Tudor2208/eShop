package org.sdi.ordermanager.controller;

import lombok.RequiredArgsConstructor;
import org.sdi.ordermanager.dto.CreateOrderRequest;
import org.sdi.ordermanager.dto.OrderResponse;
import org.sdi.ordermanager.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.sdi.ordermanager.Constants.*;

@RestController
@RequestMapping(V1 + ORDERS)
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        OrderResponse order = orderService.createOrder(createOrderRequest);
        return ResponseEntity.created(URI.create(V1 + ORDERS + SLASH + order.getOrderId())).body(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable("orderId") Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
