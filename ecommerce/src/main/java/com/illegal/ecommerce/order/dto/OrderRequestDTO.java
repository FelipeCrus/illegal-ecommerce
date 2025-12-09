package com.illegal.ecommerce.order.dto;

import java.util.List;

public record OrderRequestDTO(
        List<OrderItemDTO> items
) { }
