package com.omo.shop.service.cart;

import com.omo.shop.dto.CartItemDto;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, Integer quantity);

    void removeItemFromCart(Long cartId, Long productId);

    void updateItemQuantity(Long cartId, Long productId, Integer quantity);

    CartItemDto getCartItem(Long cartId, Long productId);
}
