package org.sdi.ordermanager.dto;

import org.sdi.ordermanager.entity.Order;
import org.sdi.ordermanager.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {

    public OrderResponse toDTO(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getId());
        orderResponse.setUserEmail(order.getUser().getEmail());
        orderResponse.setOrderDate(order.getOrderDate());

        List<OrderResponse.OrderItemResponse> itemResponses = new ArrayList<>();
        double price = 0.0;
        for (OrderItem orderItem : order.getOrderItems()) {
            OrderResponse.OrderItemResponse itemResponse = new OrderResponse.OrderItemResponse();
            price += orderItem.getProductPrice() * orderItem.getQuantity();
            itemResponse.setProductName(orderItem.getProductTitle());
            itemResponse.setProductPrice(orderItem.getProductPrice());
            itemResponse.setQuantity(orderItem.getQuantity());
            itemResponses.add(itemResponse);
        }

        orderResponse.setItems(itemResponses);
        orderResponse.setPrice(price);
        return orderResponse;
    }
}
