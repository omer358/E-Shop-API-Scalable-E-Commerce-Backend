package com.omo.shop.product.service;

import com.omo.shop.product.dto.ProductDto;
import com.omo.shop.product.model.Product;
import com.omo.shop.product.request.AddProductRequest;
import com.omo.shop.product.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {
    ProductDto addProduct(AddProductRequest product);

    ProductDto getProductById(Long id);

    void deleteProductById(Long id);

    Product updateProduct(UpdateProductRequest product, Long productId);

    List<ProductDto> getAllProducts();

    List<ProductDto> getProductsByCategory(String category);

    List<ProductDto> getProductsByBrand(String brand);

    List<ProductDto> getProductsByCategoryAndBrand(String category, String brand);

    List<ProductDto> getProductsByName(String name);

    List<ProductDto> getProductsByBrandAndName(String brand, String name);

    Long countProductsByBrandAndName(String brand, String name);

}
