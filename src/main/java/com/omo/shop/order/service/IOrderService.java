package com.omo.shop.order.service;

import com.omo.shop.order.dto.OrderDto;

import java.util.List;

public interface IOrderService {

    OrderDto placeOrder (Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
}
