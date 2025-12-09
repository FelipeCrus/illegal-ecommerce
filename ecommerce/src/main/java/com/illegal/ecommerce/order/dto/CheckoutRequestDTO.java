package com.illegal.ecommerce.order.dto;

import java.util.List;

public record CheckoutRequestDTO(
        List<OrderItemDTO> items,
        String address,
        String paymentMethod
) { }
