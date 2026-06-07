package com.ecommerce.order.controller;

import com.ecommerce.order.dto.CartItemRequestDTO;
import com.ecommerce.order.dto.CartItemResponseDTO;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartItemResponseDTO> addItemToCart(
            @RequestHeader("X-User-ID") Long userId,
            @Valid @RequestBody CartItemRequestDTO request) {
        return ResponseEntity.ok(cartService.addItemToCart(userId, request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCart(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getCartByUserId(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared successfully");
    }
}
