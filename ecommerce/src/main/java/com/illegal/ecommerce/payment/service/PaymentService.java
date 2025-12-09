package com.illegal.ecommerce.payment.service;

import com.illegal.ecommerce.payment.dto.CheckoutRequestDTO;
import com.illegal.ecommerce.payment.dto.CheckoutResponseDTO;
import com.illegal.ecommerce.order.dto.OrderRequestDTO;
import com.illegal.ecommerce.order.dto.OrderResponseDTO;
import com.illegal.ecommerce.order.model.Order;
import com.illegal.ecommerce.order.model.OrderStatus;
import com.illegal.ecommerce.order.repository.OrderRepository;
import com.illegal.ecommerce.product.repository.ProductRepository;
import com.illegal.ecommerce.user.model.User;
import com.illegal.ecommerce.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class PaymentService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    public PaymentService(OrderRepository orderRepository,
                          ProductRepository productRepository,
                          OrderService orderService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderService = orderService;
    }

    public CheckoutResponseDTO checkout(User user, CheckoutRequestDTO dto) {
        Order order = orderService.createOrderEntity(user, new OrderRequestDTO(dto.items()));
        return new CheckoutResponseDTO(order.getId(), "PAYMENT_PENDING", order.getTotal());
    }

    public OrderResponseDTO confirmPayment(Long orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));

        boolean isAdmin = user.getRole().name().equals("ROLE_ADMIN");

        if (!isAdmin && !order.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode confirmar o pedido de outro usuário.");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Pagamento só pode ser confirmado quando o pedido está PENDING");
        }

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        return OrderResponseDTO.fromOrder(order);
    }

    public OrderResponseDTO refundInternal(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));

        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.REFUNDED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido já foi cancelado ou estornado");
        }

        order.getItems().forEach(item -> {
            var product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        });

        if (order.getStatus() == OrderStatus.PAID) {
            order.setStatus(OrderStatus.REFUNDED);
        } else {
            order.setStatus(OrderStatus.CANCELLED);
        }

        orderRepository.save(order);
        return OrderResponseDTO.fromOrder(order);
    }


    public OrderResponseDTO requestRefundByCustomer(Long orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para cancelar este pedido.");
        }

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O pedido já foi enviado. Contate a loja para estorno.");
        }

        return refundInternal(orderId);
    }

    public OrderResponseDTO adminRefund(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));

        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.REFUNDED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido já está cancelado/refund.");
        }

        if (order.getStatus() != OrderStatus.SHIPPED && order.getStatus() != OrderStatus.DELIVERED && order.getStatus() != OrderStatus.PAID) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Somente pedidos enviados/entregues/pagos podem ser estornados pela loja.");
        }

        return refundInternal(orderId);
    }
}
