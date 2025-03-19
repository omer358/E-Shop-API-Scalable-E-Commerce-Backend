package com.omo.shop.service.product;

import com.omo.shop.dto.ProductDto;
import com.omo.shop.models.Product;
import com.omo.shop.request.AddProductRequest;
import com.omo.shop.request.UpdateProductRequest;

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
