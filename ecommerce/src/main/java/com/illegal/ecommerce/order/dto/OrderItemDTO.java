package com.illegal.ecommerce.order.dto;

public record OrderItemDTO(
        Long productId,
        Integer quantity
) { }
