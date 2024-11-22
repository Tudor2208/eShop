package org.sdi.ordermanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.sdi.ordermanager.dto.CartMapper;
import org.sdi.ordermanager.dto.CartPatchRequest;
import org.sdi.ordermanager.dto.CartResponse;
import org.sdi.ordermanager.entity.Cart;
import org.sdi.ordermanager.entity.CartItem;
import org.sdi.ordermanager.entity.Product;
import org.sdi.ordermanager.exception.NotFoundException;
import org.sdi.ordermanager.repository.CartRepository;
import org.sdi.ordermanager.repository.ProductRepository;
import org.sdi.ordermanager.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    @Override
    public CartResponse getCart(String cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException(String.format("Cart with id %s not found", cartId)));
        return cartMapper.toDTO(cart);
    }

    @Override
    public CartResponse patchCart(String cartId, CartPatchRequest cartPatchRequest) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException(String.format("Cart with id %s not found", cartId)));

        List<CartItem> existingItems = cart.getItems();

        for (CartPatchRequest.CartItemRequest itemRequest : cartPatchRequest.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new NotFoundException(String.format("Product with id %d not found", itemRequest.getProductId())));

            CartItem existingItem = existingItems.stream()
                    .filter(item -> item.getProduct().getId().equals(itemRequest.getProductId()))
                    .findFirst()
                    .orElse(null);

            if (itemRequest.getQuantity() == 0) {
                if (existingItem != null) {
                    existingItems.remove(existingItem);
                }
            } else {
                if (existingItem != null) {
                    existingItem.setQuantity(itemRequest.getQuantity());
                } else {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(itemRequest.getQuantity());
                    existingItems.add(newItem);
                }
            }
        }

        cart.setItems(existingItems);
        return cartMapper.toDTO(cartRepository.save(cart));
    }
}
