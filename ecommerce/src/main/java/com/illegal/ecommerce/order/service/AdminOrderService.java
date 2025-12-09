package com.illegal.ecommerce.order.service;

import com.illegal.ecommerce.order.model.Order;
import com.illegal.ecommerce.order.repository.OrderRepository;
import com.illegal.ecommerce.payment.service.AdminPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final OrderRepository orderRepository;
    private final AdminPaymentService adminPaymentService;

    public List<Order> listAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public Object confirm(Long id) {
        return adminPaymentService.forceConfirmPayment(id);
    }

    public Object refund(Long id) {
        return adminPaymentService.adminRefund(id);
    }
}


