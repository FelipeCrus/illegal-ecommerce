package com.illegal.ecommerce.payment.controller;

import com.illegal.ecommerce.order.dto.OrderResponseDTO;
import com.illegal.ecommerce.payment.dto.CheckoutRequestDTO;
import com.illegal.ecommerce.payment.dto.CheckoutResponseDTO;
import com.illegal.ecommerce.payment.service.PaymentService;
import com.illegal.ecommerce.security.UserDetailsImpl;
import com.illegal.ecommerce.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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


    @PostMapping("/confirm/{id}")
    public ResponseEntity<OrderResponseDTO> confirmPayment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl details
    ) {
        return ResponseEntity.ok(paymentService.confirmPayment(id, details.getUser()));
    }

    @PostMapping("/refund/{orderId}")
    public ResponseEntity<OrderResponseDTO> customerRefund(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserDetailsImpl details
    ) {
        User user = details.getUser();
        return ResponseEntity.ok(paymentService.requestRefundByCustomer(orderId, user));
    }

}
