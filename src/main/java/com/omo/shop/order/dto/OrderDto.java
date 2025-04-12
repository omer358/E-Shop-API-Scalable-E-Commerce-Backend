package com.omo.shop.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    Set<OrderItemDto> orderItems;
    private Long orderId;
    private Long userId;
    private LocalDateTime orderDataTime;
    private BigDecimal totalPrice;
    private String orderStatus;
}
