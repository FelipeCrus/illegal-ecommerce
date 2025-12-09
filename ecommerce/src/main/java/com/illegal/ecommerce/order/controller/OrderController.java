package com.illegal.ecommerce.order.controller;

import com.illegal.ecommerce.order.dto.OrderResponseDTO;
import com.illegal.ecommerce.order.service.OrderService;
import com.illegal.ecommerce.user.model.User;
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
    public ResponseEntity<List<OrderResponseDTO>> listOrders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.listOrders(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderDtoById(id));
    }
}
