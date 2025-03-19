package com.omo.shop.service.product;

import com.omo.shop.dto.ProductDto;
import com.omo.shop.models.Category;
import com.omo.shop.models.Product;
import com.omo.shop.service.category.CategoryMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    public static ProductDto toDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getPrice(),
                product.getInventory(),
                product.getDescription(),
                CategoryMapper.toDto(product.getCategory()),
                product.getImages()// You might want to map Category to a DTO as well
        );
    }

    public static Product toProduct(ProductDto productDto, Category category) {
        return new Product(
                productDto.getId(),
                productDto.getName(),
                productDto.getBrand(),
                productDto.getPrice(),
                productDto.getInventory(),
                productDto.getDescription(),
                category,  // Convert DTO back to Category entity
                productDto.getImages()
        );
    }

    public static List<ProductDto> toDtoList(List<Product> products) {
        return products.stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }
}
