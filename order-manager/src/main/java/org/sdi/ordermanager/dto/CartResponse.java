package org.sdi.ordermanager.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {

    private String userEmail;
    private List<CartItemResponse> items;

    @Getter
    @Setter
    @Builder
    public static class CartItemResponse {
        private int quantity;
        private Long productId;
    }
}
