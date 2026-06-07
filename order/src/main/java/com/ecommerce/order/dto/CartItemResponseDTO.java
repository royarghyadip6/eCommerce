package com.ecommerce.order.dto;

import lombok.Data;

@Data
public class CartItemResponseDTO {
    private Long cartItemId;
    private Long productId;
    private Integer quantity;
}