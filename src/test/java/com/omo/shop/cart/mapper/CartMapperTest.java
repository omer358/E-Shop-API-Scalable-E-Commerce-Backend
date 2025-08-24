package com.omo.shop.cart.mapper;

import com.omo.shop.cart.dto.CartDto;
import com.omo.shop.cart.dto.CartItemDto;
import com.omo.shop.cart.model.Cart;
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
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartMapperTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CartItemMapper cartItemMapper;

    @InjectMocks
    private CartMapper cartMapper;

    private Cart cart;
    private CartDto cartDto;
    private CartItem cartItem1;
    private CartItem cartItem2;
    private CartItemDto cartItemDto1;
    private CartItemDto cartItemDto2;

    @BeforeEach
    void setUp() {
        cart = Cart.builder()
                .id(1L)
                .totalAmount(BigDecimal.valueOf(100))
                .items(new HashSet<>())
                .build();

        cartDto = CartDto.builder()
                .id(1L)
                .totalAmount(BigDecimal.valueOf(100))
                .items(new HashSet<>())
                .build();

        cartItem1 = CartItem.builder()
                .id(1L)
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(25))
                .totalPrice(BigDecimal.valueOf(50))
                .build();

        cartItem2 = CartItem.builder()
                .id(2L)
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(50))
                .totalPrice(BigDecimal.valueOf(50))
                .build();

        cartItemDto1 = CartItemDto.builder()
                .id(1L)
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(25))
                .totalPrice(BigDecimal.valueOf(50))
                .build();

        cartItemDto2 = CartItemDto.builder()
                .id(2L)
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(50))
                .totalPrice(BigDecimal.valueOf(50))
                .build();
    }

    @Test
    @DisplayName("should map cart with multiple items to cartDto")
    void toDto_WithValidCartMultipleItems_ShouldMapCorrectly() {
        // Arrange
        Set<CartItem> items = new HashSet<>();
        items.add(cartItem1);
        items.add(cartItem2);
        cart.setItems(items);

        Set<CartItemDto> itemDtos = new HashSet<>();
        itemDtos.add(cartItemDto1);
        itemDtos.add(cartItemDto2);

        when(modelMapper.map(cart, CartDto.class)).thenReturn(cartDto);
        when(cartItemMapper.toDto(cartItem1)).thenReturn(cartItemDto1);
        when(cartItemMapper.toDto(cartItem2)).thenReturn(cartItemDto2);

        // Act
        CartDto result = cartMapper.toDto(cart);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(2);
        verify(modelMapper).map(cart, CartDto.class);
        verify(cartItemMapper).toDto(cartItem1);
        verify(cartItemMapper).toDto(cartItem2);
    }

    @Test
    @DisplayName("should return empty dto")
    void toDto_WithEmptyCart_ShouldReturnEmptyDto() {
        // Arrange
        cart.setItems(new HashSet<>());
        when(modelMapper.map(cart, CartDto.class)).thenReturn(cartDto);

        // Act
        CartDto result = cartMapper.toDto(cart);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getItems()).isEmpty();
    }

    @Test
    @DisplayName("should map cartDto with multiple items to cart")
    void toEntity_WithValidCartDtoMultipleItems_ShouldMapCorrectly() {
        // Arrange
        Set<CartItemDto> itemDtos = new HashSet<>();
        itemDtos.add(cartItemDto1);
        itemDtos.add(cartItemDto2);
        cartDto.setItems(itemDtos);

        Set<CartItem> items = new HashSet<>();
        items.add(cartItem1);
        items.add(cartItem2);

        when(modelMapper.map(cartDto, Cart.class)).thenReturn(cart);
        when(cartItemMapper.toEntity(cartItemDto1)).thenReturn(cartItem1);
        when(cartItemMapper.toEntity(cartItemDto2)).thenReturn(cartItem2);

        // Act
        Cart result = cartMapper.toEntity(cartDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(2);
        verify(modelMapper).map(cartDto, Cart.class);
        verify(cartItemMapper).toEntity(cartItemDto1);
        verify(cartItemMapper).toEntity(cartItemDto2);
    }

    @Test
    @DisplayName("should return empty entity")
    void toEntity_WithEmptyCartDto_ShouldReturnEmptyEntity() {
        // Arrange
        cartDto.setItems(new HashSet<>());
        when(modelMapper.map(cartDto, Cart.class)).thenReturn(cart);

        // Act
        Cart result = cartMapper.toEntity(cartDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getItems()).isEmpty();
    }

    @Test
    @DisplayName("should return dto with empty items when cart items are null")
    void toDto_WithNullItems_ShouldReturnEmptyDto() {
        // Arrange
        cart.setItems(null);
        when(modelMapper.map(cart, CartDto.class)).thenReturn(cartDto);

        // Act
        CartDto result = cartMapper.toDto(cart);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getItems()).isEmpty();
    }

    @Test
    @DisplayName("should return entity with empty items when cartDto items are null")
    void toEntity_WithNullItems_ShouldReturnEmptyEntity() {
        // Arrange
        cartDto.setItems(null);
        when(modelMapper.map(cartDto, Cart.class)).thenReturn(cart);

        // Act
        Cart result = cartMapper.toEntity(cartDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getItems()).isEmpty();
    }

}