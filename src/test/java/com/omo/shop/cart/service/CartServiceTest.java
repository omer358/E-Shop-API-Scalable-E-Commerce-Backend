package com.omo.shop.cart.service;

import com.omo.shop.cart.dto.CartDto;
import com.omo.shop.cart.mapper.CartMapper;
import com.omo.shop.cart.model.Cart;
import com.omo.shop.cart.model.CartItem;
import com.omo.shop.cart.repository.CartItemRepository;
import com.omo.shop.cart.repository.CartRepository;
import com.omo.shop.common.exceptions.ResourceNotFoundException;
import com.omo.shop.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartService cartService;

    private Cart cart;
    private CartDto cartDto;

    @BeforeEach
    void setup() {
        cart = new Cart();
        cart.setId(1L);
        cart.setItems(new HashSet<>());

        cartDto = new CartDto();
        cartDto.setId(1L);
        cartDto.setItems(new HashSet<>());
    }

    @Test
    void getCart_shouldReturnCartDto_whenFound() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartMapper.toDto(cart)).thenReturn(cartDto);

        CartDto result = cartService.getCart(1L);

        assertEquals(cartDto.getId(), result.getId());
        verify(cartRepository).findById(1L);
        verify(cartMapper).toDto(cart);
    }

    @Test
    void getCart_shouldThrowException_whenNotFound() {
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cartService.getCart(1L));
    }

    @Test
    void clearCart_shouldDeleteCartAndItems_whenFound() {
        cart.setUser(new User());
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        cartService.clearCart(1L);

        verify(cartItemRepository).deleteByCartId(1L);
        verify(cartRepository).delete(cart);
    }

    @Test
    void getTotalPrice_shouldCalculateTotalCorrectly() {
        CartItem item = new CartItem();
        item.setQuantity(2);
        item.setUnitPrice(BigDecimal.valueOf(10));
        item.setTotalPrice();

        cart.getItems().add(item);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartMapper.toDto(cart)).thenReturn(cartDto);
        when(cartMapper.toEntity(cartDto)).thenReturn(cart);

        BigDecimal result = cartService.getTotalPrice(1L);
        assertEquals(BigDecimal.valueOf(20), result);
    }
}
