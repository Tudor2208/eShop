package org.sdi.ordermanager.dto;

import org.sdi.ordermanager.entity.Cart;
import org.sdi.ordermanager.entity.CartItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartResponse toDTO(Cart cart) {
        return CartResponse.builder()
                .userEmail(cart.getUser().getEmail())
                .items(
                        cart.getItems().stream()
                                .map(this::toCartItemResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private CartResponse.CartItemResponse toCartItemResponse(CartItem cartItem) {
        return CartResponse.CartItemResponse.builder()
                .quantity(cartItem.getQuantity())
                .productId(cartItem.getProduct().getId())
                .build();
    }
}
