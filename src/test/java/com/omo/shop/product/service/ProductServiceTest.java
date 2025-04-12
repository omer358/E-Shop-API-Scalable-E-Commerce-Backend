package com.omo.shop.product.service;

import com.omo.shop.category.dto.CategoryDto;
import com.omo.shop.category.model.Category;
import com.omo.shop.category.repository.CategoryRepository;
import com.omo.shop.common.exceptions.ResourceNotFoundException;
import com.omo.shop.product.dto.ProductDto;
import com.omo.shop.product.mapper.ProductMapper;
import com.omo.shop.product.model.Product;
import com.omo.shop.product.repository.ProductRepository;
import com.omo.shop.product.request.AddProductRequest;
import com.omo.shop.product.request.UpdateProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.omo.shop.common.constants.ExceptionMessages.CATEGORY_NOT_FOUND;
import static com.omo.shop.common.constants.ExceptionMessages.PRODUCT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    private static final Long PRODUCT_ID = 1L;
    private static final String BRAND = "Samsung";
    private static final String NAME = "Samsung Galaxy A15";
    private static final BigDecimal PRICE = BigDecimal.valueOf(999.99);

    private Category category;
    private CategoryDto categoryDto;
    private Product product;
    private ProductDto productDto;
    private AddProductRequest addProductRequest;
    private UpdateProductRequest updateProductRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = Category.builder()
                .id(PRODUCT_ID)
                .name("Electronics")
                .products(new ArrayList<>()).build();
        categoryDto = CategoryDto.builder()
                .id(PRODUCT_ID).name("Electronics").build();

        product = Product.builder()
                .id(PRODUCT_ID).name(NAME).brand(BRAND)
                .price(PRICE).inventory(100)
                .description("Latest Galaxy model")
                .category(category).build();

        productDto = ProductDto.builder()
                .id(PRODUCT_ID).name(NAME).brand(BRAND)
                .price(PRICE).inventory(100)
                .description("Latest Galaxy model")
                .category(categoryDto).build();

        addProductRequest = AddProductRequest.builder()
                .name(NAME).brand(BRAND).price(PRICE)
                .inventory(100).description("Latest Galaxy model")
                .category(PRODUCT_ID).build();

        updateProductRequest = UpdateProductRequest.builder()
                .price(BigDecimal.valueOf(80)).inventory(300).build();
    }

    @Test
    @DisplayName("Should add product when category exists")
    void addProduct_shouldReturnProductDto_whenCategoryExists() {
        when(categoryRepository.findById(addProductRequest.getCategory()))
                .thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto result = productService.addProduct(addProductRequest);

        assertNotNull(result);
        assertEquals(NAME, result.getName());
        verify(categoryRepository).findById(PRODUCT_ID);
        verify(productRepository).save(any(Product.class));
        verify(productMapper).toDto(any(Product.class));
    }

    @Test
    @DisplayName("Should throw exception when category not found")
    void addProduct_shouldThrowException_whenCategoryNotFound() {
        when(categoryRepository.findById(PRODUCT_ID))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.addProduct(addProductRequest));

        assertTrue(exception.getMessage()
                .contains(CATEGORY_NOT_FOUND));
        verify(productRepository, never()).save(any());
        verify(productMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Should return product when found by ID")
    void getProductById_shouldReturnProductDto_whenProductExists() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto result = productService.getProductById(PRODUCT_ID);

        assertEquals(PRODUCT_ID, result.getId());
        assertEquals(NAME, result.getName());
        verify(productRepository).findById(PRODUCT_ID);
        verify(productMapper).toDto(product);
    }

    @Test
    @DisplayName("Should throw exception when product not found by ID")
    void getProductById_shouldThrowException_whenNotFound() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductById(PRODUCT_ID));

        assertEquals(PRODUCT_NOT_FOUND, exception.getMessage());
        verify(productMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Should delete product when found")
    void deleteProductById_shouldDeleteProduct_whenFound() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));

        productService.deleteProductById(PRODUCT_ID);

        verify(productRepository).delete(product);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent product")
    void deleteProductById_shouldThrowException_whenNotFound() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> productService.deleteProductById(PRODUCT_ID));

        assertEquals(PRODUCT_NOT_FOUND, exception.getMessage());
        verify(productRepository).findById(PRODUCT_ID);
        verify(productRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should update product when both product and category exist")
    void updateProduct_shouldReturnUpdatedProduct_whenProductAndCategoryExist() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        ProductDto result = productService
                .updateProduct(updateProductRequest, PRODUCT_ID);

        assertEquals(NAME, result.getName());
        verify(productRepository).save(any(Product.class));
        verify(productMapper).toDto(any(Product.class));
    }

    @Test
    @DisplayName("Should throw exception when product does not exist for update")
    void updateProduct_shouldThrowException_whenProductNotFound() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> productService.updateProduct(updateProductRequest, PRODUCT_ID));

        assertEquals(PRODUCT_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DisplayName("Should return all products")
    void getAllProducts_shouldReturnList() {
        List<Product> productList = List.of(product);
        List<ProductDto> productDtoList = List.of(productDto);

        when(productRepository.findAll()).thenReturn(productList);
        when(productMapper.toDtoList(productList)).thenReturn(productDtoList);

        List<ProductDto> result = productService.getAllProducts();

        assertEquals(1, result.size());
        verify(productRepository).findAll();
        verify(productMapper).toDtoList(productList);
    }

    @Test
    @DisplayName("Should return products by category name")
    void getProductsByCategory_shouldReturnProducts() {
        when(productRepository.findByCategoryName("Electronics")).thenReturn(List.of(product));
        when(productMapper.toDtoList(any())).thenReturn(List.of(productDto));

        List<ProductDto> result = productService.getProductsByCategory("Electronics");

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should return products by brand name")
    void getProductsByBrand_shouldReturnProducts() {
        when(productRepository.getByBrand(BRAND)).thenReturn(List.of(product));
        when(productMapper.toDtoList(any())).thenReturn(List.of(productDto));

        List<ProductDto> result = productService.getProductsByBrand(BRAND);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should return products by category and brand")
    void getProductsByCategoryAndBrand_shouldReturnProducts() {
        when(productRepository.findByCategoryNameAndBrand("Electronics", BRAND))
                .thenReturn(List.of(product));
        when(productMapper.toDtoList(any())).thenReturn(List.of(productDto));

        List<ProductDto> result = productService
                .getProductsByCategoryAndBrand("Electronics", BRAND);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should return products by name containing")
    void getProductsByName_shouldReturnMatchingProducts() {
        when(productRepository.findByNameContainingIgnoreCase("galaxy"))
                .thenReturn(List.of(product));
        when(productMapper.toDtoList(any())).thenReturn(List.of(productDto));

        List<ProductDto> result = productService.getProductsByName("galaxy");

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should return products by brand and name")
    void getProductsByBrandAndName_shouldReturnMatchingProducts() {
        when(productRepository.findByBrandAndName(BRAND, NAME)).thenReturn(List.of(product));
        when(productMapper.toDtoList(any())).thenReturn(List.of(productDto));

        List<ProductDto> result = productService.getProductsByBrandAndName(BRAND, NAME);

        assertEquals(1, result.size());
    }
}
