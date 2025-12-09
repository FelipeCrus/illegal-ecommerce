package com.illegal.ecommerce.order.dto;

import com.illegal.ecommerce.order.model.Order;
import com.illegal.ecommerce.order.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        List<OrderItemResponseDTO> items,
        Double total,
        LocalDateTime createdAt,
        OrderStatus status
) {
    public static OrderResponseDTO fromOrder(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getItems().stream()
                        .map(it -> new OrderItemResponseDTO(
                                it.getProduct().getId(),
                                it.getProduct().getName(),
                                it.getPrice(),
                                it.getQuantity()
                        )).toList(),
                order.getTotal(),
                order.getCreatedAt(),
                order.getStatus()
        );
    }

}

