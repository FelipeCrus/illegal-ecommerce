package com.illegal.ecommerce.admin.controller;

import com.illegal.ecommerce.order.dto.OrderResponseDTO;
import com.illegal.ecommerce.order.service.OrderService;
import com.illegal.ecommerce.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    public AdminController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities() == null) return false;
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ADMIN"));
    }

    @PostMapping("/orders/{orderId}/ship")
    public ResponseEntity<OrderResponseDTO> shipOrder(@PathVariable Long orderId) {
        if (!isAdmin()) return ResponseEntity.status(403).build();

        var dto = orderService.getOrderDtoById(orderId); // validate exists

        return ResponseEntity.status(501).build();
    }

    @PostMapping("/refund/{orderId}")
    public ResponseEntity<OrderResponseDTO> adminRefund(@PathVariable Long orderId) {
        if (!isAdmin()) return ResponseEntity.status(403).build();
        OrderResponseDTO dto = paymentService.adminRefund(orderId);
        return ResponseEntity.ok(dto);
    }
}
