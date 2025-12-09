package com.illegal.ecommerce.payment.dto;

import com.illegal.ecommerce.order.dto.OrderItemDTO;

import java.util.List;

public record CheckoutRequestDTO(
        List<OrderItemDTO> items,
        String address,
        String paymentMethod
) { }
