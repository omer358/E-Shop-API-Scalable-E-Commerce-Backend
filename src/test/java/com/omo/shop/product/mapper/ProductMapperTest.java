package com.omo.shop.product.mapper;

import com.omo.shop.product.dto.ProductDto;
import com.omo.shop.product.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperTest {
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper(new ModelMapper());
    }

    @Test
    @DisplayName("should return productDto when product is not null")
    void toDto_shouldReturnProductDto_whenProductIsNotNull() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(BigDecimal.valueOf(1200));

        ProductDto dto = productMapper.toDto(product);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Laptop");
        assertThat(dto.getPrice()).isEqualByComparingTo("1200");
    }

    @Test
    @DisplayName("should return product when productDto is not null")
    void toEntity_shouldReturnProduct_whenProductDtoIsNotNull() {
        ProductDto dto = new ProductDto();
        dto.setId(2L);
        dto.setName("Phone");
        dto.setPrice(BigDecimal.valueOf(800));

        Product product = productMapper.toEntity(dto);

        assertThat(product.getId()).isEqualTo(2L);
        assertThat(product.getName()).isEqualTo("Phone");
        assertThat(product.getPrice()).isEqualByComparingTo("800");
    }

    @Test
    @DisplayName("should return list of productDtos when list of products are not null")
    void toDtoList_shouldReturnListOfProductDtos_whenListOfProductsAreNotNull() {
        Product p1 = Product.builder().id(1L).name("Mouse").inventory(10).build();
        Product p2 = Product.builder().id(1L).name("Keyboard").inventory(20).build();

        List<ProductDto> dtos = productMapper.toDtoList(List.of(p1, p2));

        assertThat(dtos).hasSize(2)
                .extracting(ProductDto::getName)
                .containsExactly("Mouse", "Keyboard");
        ;
    }
}