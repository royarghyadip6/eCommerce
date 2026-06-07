package com.ecommerce.order.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long productId;
    private String name;
    private BigDecimal price;
    private Integer stockQuantity;
    private String status;
}
