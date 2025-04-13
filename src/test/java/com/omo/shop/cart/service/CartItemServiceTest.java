package com.omo.shop.cart.service;

import com.omo.shop.cart.dto.CartDto;
import com.omo.shop.cart.dto.CartItemDto;
import com.omo.shop.cart.mapper.CartItemMapper;
import com.omo.shop.cart.mapper.CartMapper;
import com.omo.shop.cart.model.Cart;
import com.omo.shop.cart.model.CartItem;
import com.omo.shop.cart.repository.CartItemRepository;
import com.omo.shop.cart.repository.CartRepository;
import com.omo.shop.product.dto.ProductDto;
import com.omo.shop.product.mapper.ProductMapper;
import com.omo.shop.product.model.Product;
import com.omo.shop.product.service.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private IProductService productService;

    @Mock
    private ICartService cartService;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CartItemMapper cartItemMapper;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartItemService cartItemService;

    private Cart cart;
    private Product product;
    private ProductDto productDto;
    private CartItem cartItem;

    @BeforeEach
    void setup() {
        cart = Cart.builder()
                .id(1L)
                .items(new HashSet<>())
                .build();

        product = Product.builder()
                .id(1L)
                .price(BigDecimal.TEN)
                .build();

        productDto = ProductDto.builder()
                .id(1L)
                .price(BigDecimal.TEN)
                .build();

        cartItem = CartItem.builder()
                .id(1L)
                .product(product)
                .quantity(1)
                .unitPrice(BigDecimal.TEN)
                .build();
        cartItem.setTotalPrice();
    }

    @Test
    @DisplayName("should add a new item to the cart when it does not already exist")
    void addItemToCart_shouldAddNewItem() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(productService.getProductById(1L)).thenReturn(mock(ProductDto.class));
        when(productMapper.toEntity(any())).thenReturn(product);

        cartItemService.addItemToCart(1L, 1L, 2);

        verify(cartItemRepository).save(any());
        verify(cartRepository).save(any());
    }

    @Test
    @DisplayName("should remove an existing item from the cart successfully")
    void removeItemFromCart_shouldRemoveItemSuccessfully() {
        // Arrange
        cart.setItems(new HashSet<>());
        cart.addItem(cartItem);

        CartItemDto cartItemDto = CartItemDto.builder()
                .id(1L)
                .product(productDto)
                .quantity(1)
                .unitPrice(BigDecimal.TEN)
                .totalPrice(BigDecimal.TEN)
                .build();

        CartDto cartDto = CartDto.builder()
                .id(1L)
                .items(Set.of(cartItemDto))
                .build();

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartService.getCart(1L)).thenReturn(cartDto);
        when(cartItemMapper.toEntity(cartItemDto)).thenReturn(cartItem);

        // Act
        cartItemService.removeItemFromCart(1L, 1L);

        // Assert
        verify(cartService).getCart(1L);
        verify(cartItemMapper).toEntity(cartItemDto);
        verify(cartRepository).save(cart);
    }

    @Test
    @DisplayName("should update the quantity of an item in the cart and recalculate total amount")
    void updateItemQuantity_shouldUpdateAndRecalculateTotal() {
        cart.getItems().add(cartItem);
        CartItem updatedItem = cart.getItems().iterator().next();
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        cartItemService.updateItemQuantity(1L, 1L, 2);

        assertEquals(3, updatedItem.getQuantity());
        verify(cartRepository).save(cart);
    }
}
