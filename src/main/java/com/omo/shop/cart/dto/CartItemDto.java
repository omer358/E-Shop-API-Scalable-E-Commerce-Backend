package com.omo.shop.cart.dto;

import com.omo.shop.product.dto.ProductDto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)  // 🔥 Avoid infinite recursion
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
