package com.omo.shop.order.repository;

import com.omo.shop.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    List<Order> findOrdersByUserId(Long userId);
}
