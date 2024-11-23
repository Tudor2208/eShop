package org.sdi.ordermanager.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequest {

    private String userEmail;
    private List<OrderItemRequest> orderItems;

    @Getter
    @Setter
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;
    }
}
