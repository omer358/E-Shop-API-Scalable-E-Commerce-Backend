package com.omo.shop.cart.repository;

import com.omo.shop.cart.model.Cart;
import com.omo.shop.user.model.User;
import com.omo.shop.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;

    private Cart cart;
    private User user;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .firstName("Omar")
                .lastName("Dev")
                .email("omar@example.com")
                .password("encoded-pass")
                .cart(cart)
                .build();

        cart = Cart.builder()
                .items(new HashSet<>())
                .user(user)
                .build();
        user = userRepository.save(user);
        cart = cartRepository.save(cart);
    }

    @Test
    @DisplayName("should return cart by user id when it exists")
    void findByUserId_shouldReturnCart_whenExists() {
        Optional<Cart> result = cartRepository.findByUserId(user.getId());

        assertThat(result).isPresent();
    }

    @Test
    @DisplayName("should return empty optional when cart does not exist by user id")
    void findByUserId_shouldReturnEmpty_whenCartNotExists() {
        Optional<Cart> result = cartRepository.findByUserId(32L);

        assertThat(result).isNotPresent();
    }
}