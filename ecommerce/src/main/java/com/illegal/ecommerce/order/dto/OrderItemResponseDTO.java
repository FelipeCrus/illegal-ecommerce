package com.illegal.ecommerce.order.dto;

public record OrderItemResponseDTO(
        Long productId,
        String name,
        Double price,
        Integer quantity
) { }
