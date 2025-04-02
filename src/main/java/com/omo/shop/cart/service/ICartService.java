package com.omo.shop.cart.service;

import com.omo.shop.cart.dto.CartDto;
import com.omo.shop.cart.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    CartDto getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Long initializeNewCart();

    Cart getCartByUserId(Long userId);
}
