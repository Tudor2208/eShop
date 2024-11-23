package org.sdi.ordermanager.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderResponse {

    private Long orderId;
    private String userEmail;
    private List<OrderItemResponse> items;

    @Getter
    @Setter
    public static class OrderItemResponse {
        private String productName;
        private Double productPrice;
        private Integer quantity;
    }

    private Double price;
    private Date orderDate;
}
