package org.sdi.ordermanager.service;

import org.sdi.ordermanager.dto.CartPatchRequest;
import org.sdi.ordermanager.dto.CartResponse;
import org.springframework.stereotype.Component;

@Component
public interface CartService {
    CartResponse getCart(String cartId);
    CartResponse patchCart(String cartId, CartPatchRequest cartPatchRequest);
}
