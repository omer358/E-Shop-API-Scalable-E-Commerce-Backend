package com.omo.shop.cart.service;

import com.omo.shop.cart.dto.CartItemDto;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, Integer quantity);

    void removeItemFromCart(Long cartId, Long productId);

    void updateItemQuantity(Long cartId, Long productId, Integer quantity);

    CartItemDto getCartItem(Long cartId, Long productId);
}
