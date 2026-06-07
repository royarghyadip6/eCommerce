package com.ecommerce.order.service.impl;

import com.ecommerce.order.dto.CartItemRequestDTO;
import com.ecommerce.order.dto.CartItemResponseDTO;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import com.ecommerce.order.service.CartService;
//import com.ecommerce.order.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
//    private final ProductClient productClient; // Verify item properties over HTTP

    @Override
    @Transactional
    public CartItemResponseDTO addItemToCart(Long userId, CartItemRequestDTO itemRequest) {
        // 1. Validate via cross-service REST call that the product actually exists
//        ProductDTO product = productClient.getProductById(itemRequest.getProductId());
//
//        if (product.getStockQuantity() < itemRequest.getQuantity()) {
//            throw new RuntimeException("Insufficient product stock available");
//        }

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, itemRequest.getProductId());

        if (existingCartItem == null) {
            CartItem newCart = new CartItem();
            newCart.setUserId(userId);
            newCart.setProductId(itemRequest.getProductId());
            newCart.setQuantity(itemRequest.getQuantity());
            newCart.setPrice(BigDecimal.valueOf(1000L * itemRequest.getQuantity()));
            existingCartItem = cartItemRepository.save(newCart);
        } else {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + itemRequest.getQuantity());
            existingCartItem.setPrice(existingCartItem.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
            cartItemRepository.save(existingCartItem);
        }

        return mapToResponseDTO(existingCartItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItem> getCartByUserId(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @Transactional
    @Override
    public void clearCart(Long userId) {
        List<CartItem> cart = cartItemRepository.findByUserId(userId);
        if (cart != null) {
            cartItemRepository.deleteByUserId(userId);
        }
    }

    private CartItemResponseDTO mapToResponseDTO(CartItem cart) {
        CartItemResponseDTO dto = new CartItemResponseDTO();
        dto.setCartItemId(cart.getCartItemId());
        dto.setProductId(cart.getProductId());
        dto.setQuantity(cart.getQuantity());
        return dto;
    }
}
