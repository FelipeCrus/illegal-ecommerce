package com.illegal.ecommerce.payment.service;

import com.illegal.ecommerce.order.model.Order;
import com.illegal.ecommerce.order.model.OrderStatus;
import com.illegal.ecommerce.order.repository.OrderRepository;
import com.illegal.ecommerce.order.dto.OrderResponseDTO;
import com.illegal.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminPaymentService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderResponseDTO forceConfirmPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Somente pedidos PENDING podem ser confirmados.");
        }

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        return OrderResponseDTO.fromOrder(order);
    }

    public OrderResponseDTO adminRefund(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));

        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.REFUNDED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido já CANC/REFUND.");
        }

        order.getItems().forEach(item -> {
            var product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        });

        order.setStatus(
                order.getStatus() == OrderStatus.PAID ? OrderStatus.REFUNDED : OrderStatus.CANCELLED
        );

        orderRepository.save(order);
        return OrderResponseDTO.fromOrder(order);
    }
}

