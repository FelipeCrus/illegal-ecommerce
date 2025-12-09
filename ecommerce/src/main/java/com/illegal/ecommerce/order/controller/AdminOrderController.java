package com.illegal.ecommerce.order.controller;

import com.illegal.ecommerce.order.model.Order;
import com.illegal.ecommerce.order.service.AdminOrderService;
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

    private final AdminOrderService adminOrderService;

    @GetMapping
    public ResponseEntity<List<Order>> listAll() {
        return ResponseEntity.ok(adminOrderService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> find(@PathVariable Long id) {
        return ResponseEntity.ok(adminOrderService.findById(id));
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<?> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(adminOrderService.confirm(id));
    }

    @PostMapping("/refund/{id}")
    public ResponseEntity<?> refund(@PathVariable Long id) {
        return ResponseEntity.ok(adminOrderService.refund(id));
    }
}
