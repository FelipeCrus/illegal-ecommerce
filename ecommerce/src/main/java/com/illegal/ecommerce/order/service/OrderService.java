package com.illegal.ecommerce.order.service;

import com.illegal.ecommerce.order.dto.OrderItemDTO;
import com.illegal.ecommerce.order.dto.OrderItemResponseDTO;
import com.illegal.ecommerce.order.dto.OrderRequestDTO;
import com.illegal.ecommerce.order.dto.OrderResponseDTO;
import com.illegal.ecommerce.order.model.Order;
import com.illegal.ecommerce.order.model.OrderItem;
import com.illegal.ecommerce.order.repository.OrderRepository;
import com.illegal.ecommerce.product.model.Product;
import com.illegal.ecommerce.product.repository.ProductRepository;
import com.illegal.ecommerce.user.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public OrderResponseDTO createOrder(User user, OrderRequestDTO dto) {
        List<OrderItem> items = new ArrayList<>();
        double total = 0;

        for (OrderItemDTO i : dto.items()) {
            Product product = productRepository.findById(i.productId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            // Validar estoque
            if (product.getStock() < i.quantity()) {
                throw new RuntimeException(
                        "Estoque insuficiente para o produto: " + product.getName()
                );
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

        orderRepository.save(order);

        List<OrderItemResponseDTO> itemsResp = items.stream()
                .map(it -> new OrderItemResponseDTO(
                        it.getProduct().getId(),
                        it.getProduct().getName(),
                        it.getPrice(),
                        it.getQuantity()))
                .collect(Collectors.toList());

        return new OrderResponseDTO(
                order.getId(),
                itemsResp,
                total,
                order.getCreatedAt()
        );
    }


    public List<OrderResponseDTO> listOrders(User user) {
        return orderRepository.findByUser(user).stream().map(order -> {
            List<OrderItemResponseDTO> itemsResp = order.getItems().stream()
                    .map(it -> new OrderItemResponseDTO(it.getProduct().getId(), it.getProduct().getName(), it.getPrice(), it.getQuantity()))
                    .collect(Collectors.toList());
            return new OrderResponseDTO(order.getId(), itemsResp, order.getTotal(), order.getCreatedAt());
        }).collect(Collectors.toList());
    }


}
