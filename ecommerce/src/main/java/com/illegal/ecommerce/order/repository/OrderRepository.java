package com.illegal.ecommerce.order.repository;


import com.illegal.ecommerce.order.model.Order;
import com.illegal.ecommerce.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
