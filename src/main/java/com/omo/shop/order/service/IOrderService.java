package com.omo.shop.order.service;

import com.omo.shop.order.model.Order;

import java.util.List;

public interface IOrderService {

    Order placeOrder (Long userId);
    Order getOrder(Long orderId);
    List<Order> getUserOrders(Long userId);
}
