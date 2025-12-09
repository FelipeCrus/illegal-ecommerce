package com.illegal.ecommerce.payment.dto;

public record CheckoutResponseDTO(
        Long orderId,
        String status,
        double total
) { }
