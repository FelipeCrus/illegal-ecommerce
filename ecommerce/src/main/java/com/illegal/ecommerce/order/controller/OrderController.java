package com.illegal.ecommerce.order.controller;

import com.illegal.ecommerce.order.dto.OrderResponseDTO;
import com.illegal.ecommerce.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> listOrders(
            @AuthenticationPrincipal com.illegal.ecommerce.security.UserDetailsImpl details) {

        return ResponseEntity.ok(orderService.listOrders(details.getUser()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal com.illegal.ecommerce.security.UserDetailsImpl details) {

        return ResponseEntity.ok(orderService.getOrderDtoById(id, details.getUser()));
    }
}

