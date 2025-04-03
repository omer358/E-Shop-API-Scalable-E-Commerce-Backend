package com.omo.shop.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    Set<OrderItemDto> orderItems;
    private Long orderId;
    private Long userId;
    private LocalDateTime orderDataTime;
    private BigDecimal TotalPrice;
    private String orderStatus;
}
