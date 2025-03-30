package com.omo.shop.service.cart;

import com.omo.shop.exceptions.ResourceNotFoundException;
import com.omo.shop.models.Cart;
import com.omo.shop.models.CartItem;
import com.omo.shop.models.Product;
import com.omo.shop.repository.CartItemRepository;
import com.omo.shop.repository.CartRepository;
import com.omo.shop.service.product.IProductService;
import com.omo.shop.service.product.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final IProductService productService;
    private final ICartService cartService;
    private final ProductMapper productMapper;

    @Override
    public void addItemToCart(Long cartId, Long productId, Integer quantity) {
        Cart cart = cartService.getCart(cartId);
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
        Cart cart = cartService.getCart(cartId);
        CartItem cartItem = getCartItem(cartId, productId);
        cart.removeItem(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, Integer quantity) {
        Cart cart = cartService.getCart(cartId);
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
                });
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(cartItem1 ->
                        cartItem1.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not found!"));
    }
}
