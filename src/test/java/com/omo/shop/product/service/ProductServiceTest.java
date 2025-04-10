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
import org.junit.jupiter.api.AfterEach;
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

import static com.omo.shop.common.constants.ExceptionMessages.PRODUCT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
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
    private CategoryDto categoryDto;
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

        categoryDto = CategoryDto.builder()
                .id(1L)
                .name("Electronics")
                .build();

        product = Product.builder()
                .id(1L)
                .name("Samsung galaxy A15")
                .brand("Samsung")
                .price(BigDecimal.valueOf(999.99))
                .inventory(100)
                .description("Latest Galaxy model")
                .category(category)
                .build();

        addProductRequest = AddProductRequest.builder()
                .name("Samsung galaxy A15")
                .brand("Samsung")
                .price(BigDecimal.valueOf(999.99))
                .inventory(100)
                .description("Latest Galaxy model")
                .category(1L)
                .build();

        updateProductRequest = UpdateProductRequest.builder()
                .price(BigDecimal.valueOf(80))
                .inventory(300)
                .build();

        productDto = ProductDto.builder()
                .id(1L)
                .name("Samsung galaxy A15")
                .brand("Samsung")
                .price(BigDecimal.valueOf(999.99))
                .inventory(100)
                .description("Latest Galaxy model")
                .category(categoryDto)
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("should return product dto when the category exist")
    void addProduct_shouldReturnProductDto_whenCategoryExist() {
        //Arrange
        when(categoryRepository.findById(addProductRequest.getCategory())).thenReturn(Optional.of(category));

        Product unsavedProduct = Product.builder()
                .name(addProductRequest.getName())
                .brand(addProductRequest.getBrand())
                .price(addProductRequest.getPrice())
                .inventory(addProductRequest.getInventory())
                .description(addProductRequest.getDescription())
                .category(category)
                .build();
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        //Act
        ProductDto result = productService.addProduct(addProductRequest);
        assertNotNull(result);
        assertEquals(productDto.getName(), result.getName());
        assertEquals(productDto.getBrand(), result.getBrand());
        assertEquals(productDto.getPrice(), result.getPrice());

        verify(categoryRepository).findById(addProductRequest.getCategory());
        verify(productRepository).save(any(Product.class));
        verify(productMapper).toDto(any(Product.class));
    }

    @Test
    @DisplayName("should throw exception when category not found")
    void addProduct_shouldThrowException_whenCategoryNotFound() {
        // Arrange
        when(categoryRepository.findById(addProductRequest.getCategory()))
                .thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                productService.addProduct(addProductRequest)
        );

        assertTrue(exception.getMessage().contains("There is no category with the id"));

        verify(categoryRepository).findById(addProductRequest.getCategory());
        verify(productRepository, never()).save(any());
        verify(productMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Should return product DTO when product exists")
    void getProductById_shouldReturnProductDto_whenProductExists() {
        //Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDto);

        //Act
        ProductDto result = productService.getProductById(product.getId());

        //Assert
        assertEquals(result.getId(), product.getId());
        assertEquals(result.getName(), product.getName());

        verify(productRepository).findById(1L);
        verify(productMapper).toDto(any());
    }

    @Test
    @DisplayName("Should throw ResourceNotFound exception when product is not found")
    void getProductById_shouldThrowException_whenProductNotFound() {
        //Given
        Long productId = 5L;
        //Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        when(productMapper.toDto(product)).thenReturn(productDto);


        //Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(productId);
        });


        assertEquals(PRODUCT_NOT_FOUND, exception.getMessage());

        verify(productRepository).findById(productId);
        verifyNoInteractions(productMapper);
    }

    @Test
    @DisplayName("Should delete product when product exists")
    void deleteProductById_ShouldDeleteProduct_WhenProductExists() {
        // Arrange
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        productService.deleteProductById(productId);

        // Assert
        verify(productRepository).delete(product);
    }

    @Test
    @DisplayName("Should throw exception when product not found")
    void deleteProductById_ShouldThrowException_WhenProductNotFound() {
        // Arrange
        Long productId = 2L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> productService.deleteProductById(productId));

        assertEquals(PRODUCT_NOT_FOUND, exception.getMessage());
        verify(productRepository, never()).delete(any());
    }


    @Test
    void updateProduct() {
        //Given
        Long productId = 1L;
        Product updatedProduct = Product.builder()
                .id(productId)
                .name(product.getName())
                .images(product.getImages())
                .brand(product.getBrand())
                .description(product.getDescription())
                .category(product.getCategory())
                // updated values
                .price(BigDecimal.valueOf(80))
                .inventory(300)
                .build();

        ProductDto updatedProductDto = ProductDto.builder()
                .id(productId)
                .name(product.getName())
                .images(productDto.getImages())
                .brand(product.getBrand())
                .description(product.getDescription())
                .category(productDto.getCategory())
                // updated values
                .price(BigDecimal.valueOf(80))
                .inventory(300)
                .build();

        //Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.toDto(any(Product.class))).thenReturn(updatedProductDto);

        //Act
        ProductDto result = productService.updateProduct(updateProductRequest, productId);

        //Assert
        assertEquals(productDto.getName(),result.getName());

        assertEquals(updatedProduct.getPrice(),result.getPrice());
        assertEquals(updatedProduct.getInventory(),result.getInventory());

        verify(productRepository).save(any(Product.class));
        verify(productMapper).toDto(any(Product.class));

    }

    @Test
    void testGetAllProducts_ReturnProductsDtoList() {
        //given
        List<Product> productList = List.of(product);
        List<ProductDto> productDtoList = List.of(productDto);

        //Arrange
        when(productRepository.findAll()).thenReturn(productList);
        when(productMapper.toDtoList(productList)).thenReturn(productDtoList);

        //Act
        List<ProductDto> result = productService.getAllProducts();

        //Assert
        assertEquals(1, result.size());
        assertEquals(productDto.getName(), result.get(0).getName());

        verify(productRepository).findAll();
        verify(productMapper).toDtoList(productList);
    }

    @Test
    @DisplayName("should return list of matched products by category")
    void getProductsByCategory_shouldReturnMatchingProductDtos() {
        //given
        String categoryName = category.getName();
        List<Product> productList = List.of(product);
        List<ProductDto> productDtoList = List.of(productDto);

        //Arrange
        when(productRepository.findByCategoryName(categoryName))
                .thenReturn(productList);
        when(productMapper.toDtoList(productList)).thenReturn(productDtoList);

        //Act
        List<ProductDto> result = productService.getProductsByCategory(categoryName);

        //Assert
        assertEquals(1, result.size());
        assertEquals(categoryName, result.get(0).getCategory().getName());

        verify(productRepository).findByCategoryName(categoryName);
        verify(productMapper).toDtoList(productList);
    }

    @Test
    @DisplayName("should return list of matched products by name")
    void getProductsByBrand_shouldReturnMatchingProductList() {
        //given
        String productBrand = product.getBrand();
        List<Product> productList = List.of(product);
        List<ProductDto> productDtoList = List.of(productDto);

        //Arrange
        when(productRepository.getByBrand(productBrand))
                .thenReturn(productList);
        when(productMapper.toDtoList(productList)).thenReturn(productDtoList);

        //Act
        List<ProductDto> result = productService.getProductsByBrand(productBrand);

        //Assert
        assertEquals(1, result.size());
        assertEquals(productBrand, result.get(0).getBrand());

        verify(productRepository).getByBrand(productBrand);
        verify(productMapper).toDtoList(productList);
    }

    @Test
    @DisplayName("should return a list of product dto that match the category and brand names")
    void getProductsByCategoryAndBrand_returnsMatchingProductsList() {
        //given
        String productBrand = product.getBrand();
        String productCategory = category.getName();

        List<Product> productList = List.of(product);
        List<ProductDto> productDtoList = List.of(productDto);

        //Arrange
        when(productRepository.findByCategoryNameAndBrand(productCategory, productBrand))
                .thenReturn(productList);
        when(productMapper.toDtoList(productList)).thenReturn(productDtoList);

        //Act
        List<ProductDto> result = productService.getProductsByCategoryAndBrand(productCategory, productBrand);

        //Assert
        //Assert
        assertEquals(1, result.size());
        assertEquals(productCategory, result.get(0).getCategory().getName());
        assertEquals(productBrand, result.get(0).getBrand());

        verify(productRepository).findByCategoryNameAndBrand(productCategory, productBrand);
        verify(productMapper).toDtoList(productList);
    }

    @Test
    @DisplayName("should return list of products like the product name")
    void getProductsByName_returnsMatchingProductsList() {
        //given
        String searchName = "galaxy";
        List<Product> productList = List.of(product);
        List<ProductDto> productDtoList = List.of(productDto);

        //Arrange
        when(productRepository.findByNameContainingIgnoreCase(searchName))
                .thenReturn(productList);
        when(productMapper.toDtoList(productList)).thenReturn(productDtoList);

        //Act
        List<ProductDto> result = productService.getProductsByName(searchName);

        //Assert
        //Assert
        assertEquals(1, result.size());
        assertEquals(productDto.getName(), result.get(0).getName());

        verify(productRepository).findByNameContainingIgnoreCase(searchName);
        verify(productMapper).toDtoList(productList);

    }

    @Test
    @DisplayName("should return list of products matching the brand and name")
    void getProductsByBrandAndName_returnsMatchingProductsList() {
        //given
        String productBrand = "Samsung";
        String productName = "Samsung galaxy A15";
        List<Product> productList = List.of(product);
        List<ProductDto> productDtoList = List.of(productDto);

        //Arrange
        when(productRepository.findByBrandAndName(productBrand, productName))
                .thenReturn(productList);
        when(productMapper.toDtoList(productList)).thenReturn(productDtoList);

        //Act
        List<ProductDto> result = productService.getProductsByBrandAndName(productBrand, productName);

        //Assert
        //Assert
        assertEquals(1, result.size());
        assertEquals(productName, result.get(0).getName());
        assertEquals(productBrand, result.get(0).getBrand());

        verify(productRepository).findByBrandAndName(productBrand, productName);
        verify(productMapper).toDtoList(productList);
    }
}