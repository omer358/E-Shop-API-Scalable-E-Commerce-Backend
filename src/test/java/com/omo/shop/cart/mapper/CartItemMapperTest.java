package com.omo.shop.cart.mapper;

import com.omo.shop.cart.dto.CartItemDto;
import com.omo.shop.cart.model.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemMapperTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CartItemMapper cartItemMapper;

    private CartItem cartItem;
    private CartItemDto cartItemDto;

    @BeforeEach
    void setUp() {
        cartItem = CartItem.builder()
                .id(1L)
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(25))
                .totalPrice(BigDecimal.valueOf(50))
                .build();

        cartItemDto = CartItemDto.builder()
                .id(1L)
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(25))
                .totalPrice(BigDecimal.valueOf(50))
                .build();
    }

    @Test
    @DisplayName("should map cartItem to cartItemDto")
    void toDto_WithValidCartItem_ShouldMapCorrectly() {
        // Arrange
        when(modelMapper.map(cartItem, CartItemDto.class)).thenReturn(cartItemDto);

        // Act
        CartItemDto result = cartItemMapper.toDto(cartItem);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCart()).isNull();
        verify(modelMapper).map(cartItem, CartItemDto.class);
        verifyNoMoreInteractions(modelMapper);
    }

    @Test
    @DisplayName("should map cartItemDto to cartItem")
    void toEntity_WithValidCartItemDto_ShouldMapCorrectly() {
        // Arrange
        when(modelMapper.map(cartItemDto, CartItem.class)).thenReturn(cartItem);

        // Act
        CartItem result = cartItemMapper.toEntity(cartItemDto);

        // Assert
        assertThat(result).isNotNull();
        verify(modelMapper).map(cartItemDto, CartItem.class);
        verifyNoMoreInteractions(modelMapper);
    }
}