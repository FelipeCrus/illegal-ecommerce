package com.illegal.ecommerce.order.service;

import com.illegal.ecommerce.order.dto.OrderRequestDTO;
import com.illegal.ecommerce.order.dto.OrderResponseDTO;
import com.illegal.ecommerce.order.model.Order;
import com.illegal.ecommerce.order.model.OrderItem;
import com.illegal.ecommerce.order.model.OrderStatus;
import com.illegal.ecommerce.order.repository.OrderRepository;
import com.illegal.ecommerce.product.model.Product;
import com.illegal.ecommerce.product.repository.ProductRepository;
import com.illegal.ecommerce.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Order createOrderEntity(User user, OrderRequestDTO dto) {
        List<OrderItem> items = new ArrayList<>();
        double total = 0;

        for (var i : dto.items()) {
            Product product = productRepository.findById(i.productId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

            if (product.getStock() < i.quantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Estoque insuficiente para o produto: " + product.getName());
            }

            OrderItem orderItem = new OrderItem(product, i.quantity(), product.getPrice());
            items.add(orderItem);

            total += product.getPrice() * i.quantity();

            product.setStock(product.getStock() - i.quantity());
            productRepository.save(product);
        }

        Order order = new Order();
        order.setUser(user);
        order.setItems(items);
        order.setTotal(total);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        return orderRepository.save(order);
    }

    public OrderResponseDTO getOrderDtoById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
        return OrderResponseDTO.fromOrder(order);
    }

    public List<OrderResponseDTO> listOrders(User user) {
        return orderRepository.findByUser(user).stream()
                .map(OrderResponseDTO::fromOrder)
                .collect(Collectors.toList());
    }
}
