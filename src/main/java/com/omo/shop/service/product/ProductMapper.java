package com.omo.shop.service.product;

import com.omo.shop.dto.ImageDto;
import com.omo.shop.dto.ProductDto;
import com.omo.shop.models.Category;
import com.omo.shop.models.Product;
import com.omo.shop.service.category.CategoryMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class ProductMapper {

    public static ProductDto toDto(Product product) {
        log.info(product.toString());
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getPrice(),
                product.getInventory(),
                product.getDescription(),
                CategoryMapper.toDto(product.getCategory()),
                Optional.ofNullable(product.getImages())
                        .orElse(Collections.emptyList()) // If null, return an empty list
                        .stream()
                        .map(ImageDto::fromEntity)
                        .collect(Collectors.toList())
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
                productDto.getImages().stream().map(ImageDto::fromDto).collect(Collectors.toList()) // ✅ Convert List<ImageDto> to List<Image>
        );
    }

    public static List<ProductDto> toDtoList(List<Product> products) {
        return products.stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }
}
