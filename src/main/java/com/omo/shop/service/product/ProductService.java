package com.omo.shop.service.product;

import com.omo.shop.dto.ProductDto;
import com.omo.shop.exceptions.ResourceNotFoundException;
import com.omo.shop.models.Category;
import com.omo.shop.models.Product;
import com.omo.shop.repository.CategoryRepository;
import com.omo.shop.repository.ProductRepository;
import com.omo.shop.request.AddProductRequest;
import com.omo.shop.request.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto addProduct(AddProductRequest request) {
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productMapper.toDto(productRepository.save(createProduct(request, category)));
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found!")
        );
        return productMapper.toDto(product);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(
                productRepository::delete,
                () -> {
                    throw new ResourceNotFoundException("Product not found");
                }
        );
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setInventory(request.getInventory());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productMapper.toDtoList(productRepository.findAll());
    }

    @Override
    public List<ProductDto> getProductsByCategory(String category) {
        return productMapper.toDtoList(productRepository.findByCategoryName(category));
    }

    @Override
    public List<ProductDto> getProductsByBrand(String brand) {
        return productMapper.toDtoList(productRepository.getByBrand(brand));
    }

    @Override
    public List<ProductDto> getProductsByCategoryAndBrand(String category, String brand) {
        return productMapper.toDtoList(productRepository.findByCategoryNameAndBrand(category, brand));
    }

    @Override
    public List<ProductDto> getProductsByName(String name) {
        return productMapper.toDtoList(productRepository.findByNameContainingIgnoreCase(name));
    }

    @Override
    public List<ProductDto> getProductsByBrandAndName(String brand, String name) {
        return productMapper.toDtoList(productRepository.findByBrandAndName(brand, name));
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
}
