package com.omo.shop.service.cart;

import com.omo.shop.dto.CartItemDto;
import com.omo.shop.models.CartItem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemMapper {
    private final ModelMapper modelMapper;

    public CartItemDto toDto(CartItem cartItem) {
        CartItemDto cartItemDto = modelMapper.map(cartItem, CartItemDto.class);

        // 🔥 Prevent infinite recursion by NOT setting CartDto
        cartItemDto.setCart(null);
        return cartItemDto;
    }

    public CartItem toEntity(CartItemDto cartItemDto) {
        return modelMapper.map(cartItemDto, CartItem.class);
    }
}
