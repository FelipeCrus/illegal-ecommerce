package com.illegal.ecommerce.order.dto;

public record CheckoutResponseDTO(
        Long orderId,
        String status,
        double total
) { }
