package com.omo.shop.product.repository;

import com.omo.shop.category.model.Category;
import com.omo.shop.product.model.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = Product.builder()
                .name("Laptop X")
                .brand("BrandX")
                .price(BigDecimal.valueOf(999.99))
                .category(Category.builder().name("Electronics").build())
                .build();

        product2 = Product.builder()
                .name("Phone Y")
                .brand("BrandY")
                .price(BigDecimal.valueOf(599.99))
                .category(Category.builder().name("Electronics").build())
                .build();

        productRepository.saveAll(List.of(product1, product2));
    }

    @Test
    @DisplayName("Should find products by category name")
    void findByCategoryName_shouldReturnProducts() {
        List<Product> result = productRepository.findByCategoryName("Electronics");

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should find products by brand")
    void getByBrand_shouldReturnProducts() {
        List<Product> result = productRepository.getByBrand("BrandX");

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getBrand()).isEqualTo("BrandX");
    }

    @Test
    @DisplayName("Should find products by category name and brand")
    void findByCategoryNameAndBrand_shouldReturnProducts() {
        List<Product> result = productRepository.findByCategoryNameAndBrand("Electronics", "BrandX");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Laptop X");
    }

    @Test
    @DisplayName("Should find products by name containing ignore case")
    void findByNameContainingIgnoreCase_shouldReturnMatchingProducts() {
        List<Product> result = productRepository.findByNameContainingIgnoreCase("laptop");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Laptop X");
    }

    @Test
    @DisplayName("Should find products by brand and name")
    void findByBrandAndName_shouldReturnProduct() {
        List<Product> result = productRepository.findByBrandAndName("BrandX", "Laptop X");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Laptop X");
        assertThat(result.get(0).getBrand()).isEqualTo("BrandX");
    }

    @Test
    @DisplayName("Should count products by brand and name")
    void countByBrandAndName_shouldReturnCorrectCount() {
        Long count = productRepository.countByBrandAndName("BrandX", "Laptop X");

        assertThat(count).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should not find products for wrong brand and category")
    void findByCategoryNameAndBrand_shouldReturnEmpty_whenNoMatch() {
        List<Product> result = productRepository.findByCategoryNameAndBrand("Furniture", "BrandZ");

        assertThat(result).isEmpty();
    }
}
