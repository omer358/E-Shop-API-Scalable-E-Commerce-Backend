package com.omo.shop.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)  // 🔥 Avoid infinite recursion
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    @EqualsAndHashCode.Include
    private Long id;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private ProductDto product;
    @EqualsAndHashCode.Exclude  // 🔥 Exclude `cart` to prevent infinite recursion
    private CartDto cart;
}
