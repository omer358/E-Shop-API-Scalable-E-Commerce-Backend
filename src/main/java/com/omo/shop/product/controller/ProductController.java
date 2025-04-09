package com.omo.shop.product.controller;

import com.omo.shop.common.response.ApiResponse;
import com.omo.shop.exceptions.ResourceNotFoundException;
import com.omo.shop.product.dto.ProductDto;
import com.omo.shop.product.model.Product;
import com.omo.shop.product.request.AddProductRequest;
import com.omo.shop.product.request.UpdateProductRequest;
import com.omo.shop.product.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class  ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse("Success", products));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable("productId") Long id) {
        try {
            ProductDto product = productService.getProductById(id);
            return ResponseEntity.ok(new ApiResponse("products", product));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(
                    new ApiResponse(e.getMessage(), NOT_FOUND)
            );
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            ProductDto newProduct = productService.addProduct(product);
            return ResponseEntity.ok(new ApiResponse("Success", newProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(
            @PathVariable("productId") Long id,
            @RequestBody UpdateProductRequest updatedProduct
    ) {
        try {
            ProductDto productDto = productService.updateProduct(updatedProduct, id);
            return ResponseEntity.ok(
                    new ApiResponse("product updated successfully", productDto)
            );
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(
            @PathVariable("productId") Long id
    ) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("Deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

    }

    @GetMapping("/by-brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(
            @RequestParam String brandName,
            @RequestParam String productName
    ) {
        List<ProductDto> products = productService.getProductsByBrandAndName(brandName, productName);
        return ResponseEntity.ok(new ApiResponse("Success", products));
    }

    @GetMapping("/by-brand")
    public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brand) {
        List<ProductDto> products = productService.getProductsByBrand(brand);
        return ResponseEntity.ok(new ApiResponse("Success", products));
    }

    @GetMapping("/by-category")
    public ResponseEntity<ApiResponse> getProductByCategory(@RequestParam String category) {
        List<ProductDto> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(new ApiResponse("success", products));
    }

    @GetMapping("/by-name")
    public ResponseEntity<ApiResponse> getProductsByName(@RequestParam("name") String productName) {
        List<ProductDto> products = productService.getProductsByName(productName);
        return ResponseEntity.ok(new ApiResponse("Success", products));
    }
}
