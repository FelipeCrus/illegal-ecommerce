package com.illegal.ecommerce.order.controller;

import com.illegal.ecommerce.order.dto.OrderRequestDTO;
import com.illegal.ecommerce.order.dto.OrderResponseDTO;
import com.illegal.ecommerce.order.service.OrderService;
import com.illegal.ecommerce.user.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public OrderResponseDTO createOrder(@AuthenticationPrincipal User user, @RequestBody OrderRequestDTO dto) {
        return service.createOrder(user, dto);
    }

    @GetMapping
    public List<OrderResponseDTO> listOrders(@AuthenticationPrincipal User user) {
        return service.listOrders(user);
    }
}
