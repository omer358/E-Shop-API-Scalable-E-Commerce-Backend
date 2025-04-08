package com.omo.shop.product.service;

import com.omo.shop.category.dto.CategoryDto;
import com.omo.shop.category.model.Category;
import com.omo.shop.category.repository.CategoryRepository;
import com.omo.shop.product.dto.ProductDto;
import com.omo.shop.product.mapper.ProductMapper;
import com.omo.shop.product.model.Product;
import com.omo.shop.product.repository.ProductRepository;
import com.omo.shop.product.request.AddProductRequest;
import com.omo.shop.product.request.UpdateProductRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Category category;
    private Product product;
    private ProductDto productDto;
    private AddProductRequest addProductRequest;
    private UpdateProductRequest updateProductRequest;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = Category.builder()
                .id(1L)
                .name("Electronics")
                .products(new ArrayList<>())
                .build();

        product = Product.builder()
                .id(1L)
                .name("Smartphone")
                .brand("Samsung")
                .price(BigDecimal.valueOf(999.99))
                .inventory(100)
                .description("Latest Galaxy model")
                .category(category)
                .build();

        addProductRequest = AddProductRequest.builder()
                .name("Smartphone")
                .brand("Samsung")
                .price(BigDecimal.valueOf(999.99))
                .inventory(100)
                .description("Latest Galaxy model")
                .category(1L)
                .build();

        productDto = ProductDto.builder()
                .id(1L)
                .name("Smartphone")
                .brand("Samsung")
                .price(BigDecimal.valueOf(999.99))
                .inventory(100)
                .description("Latest Galaxy model")
                .category(new CategoryDto())
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addProduct() {
    }

    @Test
    void getProductById() {
        //When
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDto);
        ProductDto productDto1 = productService.getProductById(product.getId());
        assertEquals(productDto1.getId(),product.getId());
        assertEquals(productDto1.getName(),product.getName());

        verify(productRepository).findById(any());
        verify(productMapper).toDto(any()z);
    }

    @Test
    void deleteProductById() {
    }

    @Test
    void updateProduct() {
    }

    @Test
    void testGetAllProducts_ReturnProductsDtoList() {
        //given
        List<Product> productList = List.of(product);
        List<ProductDto> productDtoList = List.of(productDto);

        //mock the calls
        when(productRepository.findAll()).thenReturn(productList);
        when(productMapper.toDtoList(productList)).thenReturn(productDtoList);

        //when
        List<ProductDto> result = productService.getAllProducts();

        //then
        assertEquals(1, result.size());
        assertEquals("Smartphone", result.get(0).getName());

        verify(productRepository).findAll();
        verify(productMapper).toDtoList(productList);
    }

    @Test
    void getProductsByCategory() {
    }

    @Test
    void getProductsByBrand() {
    }

    @Test
    void getProductsByCategoryAndBrand() {
    }

    @Test
    void getProductsByName() {
    }

    @Test
    void getProductsByBrandAndName() {
    }

    @Test
    void countProductsByBrandAndName() {
    }
}