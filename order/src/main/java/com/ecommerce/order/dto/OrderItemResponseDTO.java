package com.ecommerce.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponseDTO {
    private Long orderItemId;
    private Long productId;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
    private BigDecimal subTotal;
}