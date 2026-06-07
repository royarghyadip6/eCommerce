package com.ecommerce.order.service;

import com.ecommerce.order.dto.CartItemRequestDTO;
import com.ecommerce.order.dto.CartItemResponseDTO;
import com.ecommerce.order.model.CartItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartService {
    CartItemResponseDTO addItemToCart(Long userId, CartItemRequestDTO itemRequest);

    List<CartItem> getCartByUserId(Long userId);

    @Transactional
    void clearCart(Long userId);
}