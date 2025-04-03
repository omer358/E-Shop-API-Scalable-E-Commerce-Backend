package com.omo.shop.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public class OrderDto {
    Set<OrderItemDto> orderItems;
    private Long orderId;
    private Long userId;
    private LocalDateTime orderDataTime;
    private BigDecimal TotalPrice;
    private String orderStatus;
}
