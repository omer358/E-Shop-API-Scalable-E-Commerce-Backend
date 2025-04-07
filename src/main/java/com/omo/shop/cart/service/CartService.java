package com.omo.shop.cart.service;

import com.omo.shop.cart.dto.CartDto;
import com.omo.shop.cart.mapper.CartMapper;
import com.omo.shop.cart.model.Cart;
import com.omo.shop.cart.model.CartItem;
import com.omo.shop.cart.repository.CartItemRepository;
import com.omo.shop.cart.repository.CartRepository;
import com.omo.shop.exceptions.ResourceNotFoundException;
import com.omo.shop.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;


    @Override
    public CartDto getCart(Long id) {

        Cart cart = cartRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Cart not found!")
                );
        return cartMapper.toDto(cart);
    }

    //TODO: ATTENTION: THE PROGAGATION MAY LEAD TO INCONSISTENCIES IF THE CALLING METHOD `placeOrder failed
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void clearCart(Long id) {
        // Delete all cart items first
        cartItemRepository.deleteByCartId(id);

        Optional<Cart> cartOptional = cartRepository.findById(id);
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            // Remove the reference from the user before deleting the cart
            if (cart.getUser() != null) {
                cart.getUser().setCart(null);
            }
            cartRepository.delete(cart);
        } else {
            throw new ResourceNotFoundException("Cart not found for ID: " + id);
        }
    }


    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = cartMapper.toEntity(getCart(id));
        return cart.getItems()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Cart initializeNewCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("cart not found!"));
    }
}
