package com.omo.shop.category.repository;

import com.omo.shop.category.model.Category;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .name("Electronics")
                .build();
        categoryRepository.save(category);
    }

    @Test
    @DisplayName("should return category by name when it exists")
    void findByName_shouldReturnCategory_whenExists() {
        Optional<Category> result = categoryRepository.findByName("Electronics");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Electronics");
    }

    @Test
    @DisplayName("should return empty optional when category name does not exist")
    void findByName_shouldReturnEmpty_whenNotExists() {
        Optional<Category> result = categoryRepository.findByName("NonExistent");

        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("should return true when category name exists")
    void existsByName_shouldReturnTrue_whenCategoryExists() {
        boolean exists = categoryRepository.existsByName("Electronics");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("should return false when category name does not exist")
    void existsByName_shouldReturnFalse_whenCategoryNotExists() {
        boolean exists = categoryRepository.existsByName("Books");

        assertThat(exists).isFalse();
    }
}
