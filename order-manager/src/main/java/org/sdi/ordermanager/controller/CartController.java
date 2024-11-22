package org.sdi.ordermanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sdi.ordermanager.dto.CartPatchRequest;
import org.sdi.ordermanager.dto.CartResponse;
import org.sdi.ordermanager.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.sdi.ordermanager.Constants.CARTS;
import static org.sdi.ordermanager.Constants.V1;

@RestController
@RequestMapping(V1 + CARTS)
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable(value="userId") String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<CartResponse> patchCart(@PathVariable(value="userId") String userId,
                                                  @Valid @RequestBody CartPatchRequest cartPatchRequest) {
        return ResponseEntity.ok(cartService.patchCart(userId, cartPatchRequest));
    }
}
