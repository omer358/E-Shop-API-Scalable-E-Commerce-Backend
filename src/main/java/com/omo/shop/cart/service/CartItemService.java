package com.omo.shop.cart.service;

import com.omo.shop.cart.dto.CartDto;
import com.omo.shop.cart.dto.CartItemDto;
import com.omo.shop.cart.mapper.CartItemMapper;
import com.omo.shop.cart.mapper.CartMapper;
import com.omo.shop.cart.model.Cart;
import com.omo.shop.cart.model.CartItem;
import com.omo.shop.cart.repository.CartItemRepository;
import com.omo.shop.cart.repository.CartRepository;
import com.omo.shop.common.constants.ExceptionMessages;
import com.omo.shop.common.exceptions.ResourceNotFoundException;
import com.omo.shop.product.mapper.ProductMapper;
import com.omo.shop.product.model.Product;
import com.omo.shop.product.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.omo.shop.common.constants.ExceptionMessages.CART_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final IProductService productService;
    private final ICartService cartService;
    private final ProductMapper productMapper;
    private final CartItemMapper cartItemMapper;
    private final CartMapper cartMapper;

    @Override
    public void addItemToCart(Long cartId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException(CART_NOT_FOUND));
        Product product = productMapper.toEntity(productService.getProductById(productId));
        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(new CartItem());
        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);

    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() ->
                new ResourceNotFoundException(CART_NOT_FOUND));
        CartItem cartItem = cartItemMapper.toEntity(getCartItem(cartId, productId));
        cart.removeItem(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(CART_NOT_FOUND));
        cart.getItems()
                .stream()
                .filter(cartItem -> cartItem
                        .getProduct()
                        .getId()
                        .equals(productId))
                .findFirst()
                .ifPresent(cartItem -> {
                    cartItem.setQuantity(cartItem.getQuantity() + quantity);
                    cartItem.setUnitPrice(cartItem.getProduct().getPrice());
                    cartItem.setTotalPrice();
                    //FIX: problem with setting the total amount to the cart after updating the cartitem
                });
        BigDecimal totalAmount = cart.getItems().stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItemDto getCartItem(Long cartId, Long productId) {
        CartDto cart = cartService.getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(cartItem1 ->
                        cartItem1.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.CART_ITEM_NOT_FOUND));
    }
}
