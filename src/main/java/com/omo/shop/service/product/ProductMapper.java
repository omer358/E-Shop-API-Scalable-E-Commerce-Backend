package com.omo.shop.service.product;

import com.omo.shop.dto.ProductDto;
import com.omo.shop.models.Product;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductMapper {

    private final ModelMapper modelMapper;

    public ProductDto toDto(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }

    public Product toEntity(ProductDto productDto) {
        return modelMapper.map(productDto, Product.class);
    }

    public List<ProductDto> toDtoList(List<Product> products) {
        return products.stream().map(this::toDto).collect(Collectors.toList());
    }
}
