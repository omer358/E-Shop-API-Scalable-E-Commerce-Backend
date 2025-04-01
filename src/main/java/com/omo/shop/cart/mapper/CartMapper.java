package com.omo.shop.cart.mapper;

import com.omo.shop.cart.dto.CartDto;
import com.omo.shop.cart.dto.CartItemDto;
import com.omo.shop.cart.model.Cart;
import com.omo.shop.cart.model.CartItem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartMapper {
    private final ModelMapper modelMapper;
    private final CartItemMapper cartItemMapper;

    public CartDto toDto(Cart cart) {
        CartDto cartDto = modelMapper.map(cart, CartDto.class);

        // Manually map items to prevent infinite recursion
        Set<CartItemDto> cartItemDtos = cart.getItems()
                .stream()
                .map(cartItemMapper::toDto) // Convert CartItem to CartItemDto
                .collect(Collectors.toSet());

        cartDto.setItems(cartItemDtos);
        return cartDto;
    }

    public Cart toEntity(CartDto cartDto) {
        Cart cart = modelMapper.map(cartDto, Cart.class);

        // Manually map items
        Set<CartItem> cartItems = cartDto.getItems()
                .stream()
                .map(cartItemMapper::toEntity) // Convert CartItemDto to CartItem
                .collect(Collectors.toSet());

        cart.setItems(cartItems);
        return cart;
    }
}
