package com.illegal.ecommerce.payment.controller;

import com.illegal.ecommerce.order.dto.OrderResponseDTO;
import com.illegal.ecommerce.payment.dto.CheckoutRequestDTO;
import com.illegal.ecommerce.payment.dto.CheckoutResponseDTO;
import com.illegal.ecommerce.payment.service.PaymentService;
import com.illegal.ecommerce.security.UserDetailsImpl;
import com.illegal.ecommerce.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponseDTO> checkout(
            @AuthenticationPrincipal UserDetailsImpl details,
            @RequestBody CheckoutRequestDTO dto
    ) {
        User user = details.getUser();
        return ResponseEntity.ok(paymentService.checkout(user, dto));
    }


    @PostMapping("/confirm/{orderId}")
    public ResponseEntity<OrderResponseDTO> confirmPayment(
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(paymentService.confirmPayment(orderId));
    }

    @PostMapping("/refund/{orderId}")
    public ResponseEntity<OrderResponseDTO> customerRefund(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserDetailsImpl details
    ) {
        User user = details.getUser();
        return ResponseEntity.ok(paymentService.requestRefundByCustomer(orderId, user));
    }


    @PostMapping("/admin/refund/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> adminRefund(
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(paymentService.adminRefund(orderId));
    }
}
