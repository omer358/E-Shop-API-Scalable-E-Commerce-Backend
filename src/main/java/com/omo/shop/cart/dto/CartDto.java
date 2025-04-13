package com.omo.shop.cart.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)  // 🔥 Avoid infinite recursion
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
    private Long id;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private Set<CartItemDto> items = new HashSet<CartItemDto>() {
    };


}
