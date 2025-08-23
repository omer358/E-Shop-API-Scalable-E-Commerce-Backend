package com.omo.shop.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omo.shop.category.dto.CategoryDto;
import com.omo.shop.common.exceptions.ResourceNotFoundException;
import com.omo.shop.product.dto.ProductDto;
import com.omo.shop.product.request.AddProductRequest;
import com.omo.shop.product.request.UpdateProductRequest;
import com.omo.shop.product.service.ProductService;
import com.omo.shop.security.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static com.omo.shop.common.constants.ExceptionMessages.CATEGORY_NOT_FOUND;
import static com.omo.shop.common.constants.ExceptionMessages.PRODUCT_NOT_FOUND;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtService jwtService;


    private ProductDto productDto;
    private AddProductRequest addProductRequest;
    private List<ProductDto> productDtoList;

    @BeforeEach
    void setUp() {
        productDto = ProductDto.builder()
                .id(1L)
                .name("Test Product")
                .brand("Test Brand")
                .price(BigDecimal.valueOf(99.99))
                .inventory(10)
                .description("Test Description")
                .category(CategoryDto.builder().id(1L).name("Test Category").build())
                .images(Collections.emptyList())
                .build();
        addProductRequest = AddProductRequest.builder()
                .name("Test Product")
                .brand("Test Brand")
                .price(BigDecimal.valueOf(99.99))
                .inventory(10)
                .description("Test Description")
                .build();
        productDtoList = Collections.singletonList(productDto);
    }

    @Test
    @DisplayName("Should create new product")
    void createProduct_Success() throws Exception {
        when(productService.addProduct(any(AddProductRequest.class))).thenReturn(productDto);

        mockMvc.perform(post("/api/v1/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addProductRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name", is(productDto.getName())));
    }

    @Test
    @DisplayName("should return not found when category does not exist")
    void createProduct_BadRequest() throws Exception {
        when(productService.addProduct(any(AddProductRequest.class))).thenThrow(new ResourceNotFoundException(CATEGORY_NOT_FOUND));
        mockMvc.perform(post("/api/v1/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AddProductRequest())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",is(CATEGORY_NOT_FOUND)));
    }

    @Test
    @DisplayName("Should update product")
    void updateProduct_Success() throws Exception {
        UpdateProductRequest updateProductRequest = UpdateProductRequest.builder()
                .name("updated Product")
                .brand("Test Brand")
                .price(BigDecimal.valueOf(99.99))
                .inventory(10)
                .description("Test Description")
                .category(1L)
                .build();
        when(productService.updateProduct(updateProductRequest, 1L)).thenReturn(productDto);

        mockMvc.perform(put("/api/v1/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProductRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is(productDto.getName())));

        verify(productService).updateProduct(updateProductRequest, 1L);
        verifyNoMoreInteractions(productService);
    }

    @Test
    @DisplayName("Should return not found when product does not exist")
    void updateProduct_NotFound() throws Exception {
        UpdateProductRequest updateProductRequest = UpdateProductRequest.builder()
                .name("updated Product")
                .brand("Test Brand")
                .price(BigDecimal.valueOf(99.99))
                .inventory(10)
                .description("Test Description")
                .category(1L)
                .build();
        when(productService.updateProduct(updateProductRequest, 1L))
                .thenThrow(new ResourceNotFoundException(PRODUCT_NOT_FOUND));
        mockMvc.perform(put("/api/v1/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProductRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",is(PRODUCT_NOT_FOUND)));
        verify(productService).updateProduct(updateProductRequest, 1L);
        verifyNoMoreInteractions(productService);
    }

    @Test
    @DisplayName("Should get product by id")
    void getProductById_Success() throws Exception {
        when(productService.getProductById(1L)).thenReturn(productDto);

        mockMvc.perform(get("/api/v1/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is(productDto.getName())));
    }

    @Test
    @DisplayName("Should return not found when product does not exist")
    void getProductById_NotFound() throws Exception {
        when(productService.getProductById(1L)).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(get("/api/v1/products/{id}", 1L))
                .andExpect(status().isNotFound());
        verify(productService).getProductById(1L);
        verifyNoMoreInteractions(productService);
    }

    @Test
    @DisplayName("Should get all products")
    void getAllProducts_Success() throws Exception {
        when(productService.getAllProducts()).thenReturn(productDtoList);

        mockMvc.perform(get("/api/v1/products/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", is(productDto.getName())));
    }

    @Test
    @DisplayName("Should delete product by id")
    void deleteProduct_Success() throws Exception {
        doNothing().when(productService).deleteProductById(1L);

        mockMvc.perform(delete("/api/v1/products/{id}", 1L))
                .andExpect(status().isOk());

        verify(productService).deleteProductById(1L);
    }

    @Test
    @DisplayName("Should return not found when product does not exist")
    void deleteProduct_NotFound() throws Exception {
        doThrow(ResourceNotFoundException.class).when(productService).deleteProductById(1L);
        mockMvc.perform(delete("/api/v1/products/{id}", 1L))
                .andExpect(status().isNotFound());
        verify(productService).deleteProductById(1L);
        verifyNoMoreInteractions(productService);
    }

    @Test
    @DisplayName("Should get products by category")
    void getProductsByCategory_Success() throws Exception {
        when(productService.getProductsByCategory("Test Category")).thenReturn(productDtoList);

        mockMvc.perform(get("/api/v1/products/by-category")
                        .param("category", "Test Category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", is(productDto.getName())));
    }

    @Test
    @DisplayName("Should get products by brand")
    void getProductsByBrand_Success() throws Exception {
        when(productService.getProductsByBrand("Test Brand")).thenReturn(productDtoList);

        mockMvc.perform(get("/api/v1/products/by-brand")
                        .param("brand", "Test Brand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", is(productDto.getName())));
    }

    @Test
    @DisplayName("Should get products by name")
    void getProductsByName_Success() throws Exception {
        when(productService.getProductsByName("Test")).thenReturn(productDtoList);

        mockMvc.perform(get("/api/v1/products/by-name")
                .param("name", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", is(productDto.getName())));
    }

    @Test
    @DisplayName("Should get products by brand and name")
    void getProductsByBrandAndName_Success() throws Exception {
        when(productService.getProductsByBrandAndName("Test Brand", "Test"))
                .thenReturn(productDtoList);

        mockMvc.perform(get("/api/v1/products/by-brand-and-name")
                .param("brandName", "Test Brand")
                .param("productName", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", is(productDto.getName())));
    }
}