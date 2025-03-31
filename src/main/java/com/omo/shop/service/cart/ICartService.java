package com.omo.shop.service.cart;

import com.omo.shop.dto.CartDto;

import java.math.BigDecimal;

public interface ICartService {
    CartDto getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Long initializeNewCart();
}
