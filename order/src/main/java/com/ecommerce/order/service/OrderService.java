package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderRequestDTO;
import com.ecommerce.order.dto.OrderResponseDTO;
import com.ecommerce.order.model.OrderStatus;
import java.util.List;

public interface OrderService {
    OrderResponseDTO placeOrder(OrderRequestDTO requestDTO);
    OrderResponseDTO getOrderById(Long orderId);
    List<OrderResponseDTO> getOrdersByUserId(Long userId);
    OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus status);
}