package com.illegal.ecommerce.admin.controller;

import com.illegal.ecommerce.order.model.Order;
import com.illegal.ecommerce.order.repository.OrderRepository;
import com.illegal.ecommerce.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<Order>> listAll() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> find(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        return ResponseEntity.ok(order);
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<?> forceConfirm(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.confirmPayment(id));
    }

    @PostMapping("/refund/{id}")
    public ResponseEntity<?> forceRefund(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.adminRefund(id));
    }
}
