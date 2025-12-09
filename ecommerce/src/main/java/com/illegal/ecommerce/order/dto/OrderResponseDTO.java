package com.illegal.ecommerce.order.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        List<OrderItemResponseDTO> items,
        Double total,
        LocalDateTime createdAt
) { }
