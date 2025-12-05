package com.illegal.ecommerce.order.service;

import com.illegal.ecommerce.order.dto.OrderItemResponseDTO;
import com.illegal.ecommerce.order.dto.OrderRequestDTO;
import com.illegal.ecommerce.order.dto.OrderResponseDTO;
import com.illegal.ecommerce.order.model.Order;
import com.illegal.ecommerce.order.model.OrderItem;
import com.illegal.ecommerce.order.repository.OrderRepository;
import com.illegal.ecommerce.product.repository.ProductRepository;
import com.illegal.ecommerce.user.model.User;
import org.springframework.stereotype.Service;

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
        List<OrderItem> items = dto.items().stream().map(i -> {
            var product = productRepository.findById(i.productId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
            return new OrderItem(product, i.quantity(), product.getPrice());
        }).collect(Collectors.toList());

        double total = items.stream().mapToDouble(it -> it.getPrice() * it.getQuantity()).sum();

        Order order = new Order();
        order.setUser(user);
        order.setItems(items);
        order.setTotal(total);

        orderRepository.save(order);

        List<OrderItemResponseDTO> itemsResp = items.stream()
                .map(it -> new OrderItemResponseDTO(it.getProduct().getId(), it.getProduct().getName(), it.getPrice(), it.getQuantity()))
                .collect(Collectors.toList());

        return new OrderResponseDTO(order.getId(), itemsResp, total, order.getCreatedAt());
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
