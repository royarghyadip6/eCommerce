package com.ecommerce.order.service.impl;

import com.ecommerce.order.dto.OrderItemResponseDTO;
import com.ecommerce.order.dto.OrderRequestDTO;
import com.ecommerce.order.dto.OrderResponseDTO;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.CartService;
import com.ecommerce.order.service.OrderService;
//import com.ecommerce.product.model.Product;
//import com.ecommerce.product.model.ProductStatus;
//import com.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    @Override
    @Transactional
    public OrderResponseDTO placeOrder(OrderRequestDTO requestDTO) {
        log.info("Processing checkout initialization request for User ID: {}", requestDTO.getUserId());

        List<CartItem> cartItems = cartService.getCartByUserId(requestDTO.getUserId());
        if (cartItems.isEmpty()) {

        }

        BigDecimal totalPrice = cartItems.stream().map(CartItem::getPrice).reduce(BigDecimal.ZERO,BigDecimal::add);

        Order order = new Order();
        order.setUserId(requestDTO.getUserId());
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream().map(
                cartItem -> new OrderItem(
                        null,
                        order,
                        cartItem.getProductId(),
                        cartItem.getQuantity(),
                        cartItem.getPrice()
                )).toList();
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        log.info("Successfully checked out Order ID: {}", savedOrder.getOrderId());
        cartService.clearCart(savedOrder.getUserId());

        return mapToResponseDTO(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        return mapToResponseDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        order.setStatus(status);
        return mapToResponseDTO(orderRepository.save(order));
    }

    // --- Object Transformation Helpers ---

    private OrderResponseDTO mapToResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUserId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());

        List<OrderItemResponseDTO> itemDtos = order.getOrderItems().stream().map(item -> {
            OrderItemResponseDTO itemDto = new OrderItemResponseDTO();
            itemDto.setOrderItemId(item.getOrderItemId());
            itemDto.setProductId(item.getProductId());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPriceAtPurchase(item.getPriceAtPurchase());
            return itemDto;
        }).collect(Collectors.toList());

        dto.setItems(itemDtos);
        return dto;
    }
}