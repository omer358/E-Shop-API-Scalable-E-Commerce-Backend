package com.omo.shop.user.repository;

import com.omo.shop.user.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Transactional
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .firstName("Omar")
                .lastName("Dev")
                .email("omar@example.com")
                .password("encoded-pass")
                .build();
        userRepository.save(user);
    }

    @Test
    void existsByEmail_shouldReturnTrue_WhenUserExistWithEmail() {
        boolean result = userRepository.existsByEmail("omar@example.com");
        assertTrue(result);
    }

    @Test
    void existsByEmail_shouldReturnFalse_WhenUserNotExistWithEmail() {
        boolean result = userRepository.existsByEmail("mohamed@example.com");
        assertFalse(result);
    }

    @Test
    @DisplayName("should return user by email when it exists")
    void findByEmail_shouldReturnUser_whenExists() {
        Optional<User> result = userRepository.findByEmail("omar@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("Omar");
    }

    @Test
    @DisplayName("should return empty optional when user does not exists")
    void findByEmail_shouldReturnEmpty_whenNotExists() {
        Optional<User> result = userRepository.findByEmail("mohamed@example.com");

        assertThat(result).isNotPresent();
    }
}