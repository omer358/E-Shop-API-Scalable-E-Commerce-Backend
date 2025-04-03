package com.omo.shop.order.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemDto {
    private Long id;
    private Long productId;
    private BigDecimal price;
    private int quantity;
}
