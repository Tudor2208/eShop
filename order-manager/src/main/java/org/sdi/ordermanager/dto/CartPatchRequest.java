package org.sdi.ordermanager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartPatchRequest {

    @NotNull(message="Items cannot be null")
    private List<CartItemRequest> items;

    @Getter
    @Setter
    @Builder
    public static class CartItemRequest {
        private Long productId;
        private int quantity;
    }
}
