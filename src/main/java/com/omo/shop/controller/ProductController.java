package com.omo.shop.controller;

import com.omo.shop.exceptions.ResourceNotFoundException;
import com.omo.shop.models.Product;
import com.omo.shop.request.AddProductRequest;
import com.omo.shop.request.UpdateProductRequest;
import com.omo.shop.response.ApiResponse;
import com.omo.shop.service.product.IProductService;
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
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse("Success", products));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable("productId") Long id) {
        try {
            Product product = productService.getProductById(id);
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
            Product newProduct = productService.addProduct(product);
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
            Product product = productService.updateProduct(updatedProduct, id);
            return ResponseEntity.ok(
                    new ApiResponse("product updated successfully", product)
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
        List<Product> products = productService.getProductsByBrandAndName(brandName, productName);
        return ResponseEntity.ok(new ApiResponse("Success", products));
    }

    @GetMapping("/by-brand")
    public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brand) {
        List<Product> products = productService.getProductsByBrand(brand);
        return ResponseEntity.ok(new ApiResponse("Success", products));
    }

    @GetMapping("/by-category")
    public ResponseEntity<ApiResponse> getProductByCategory(@RequestParam String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(new ApiResponse("success", products));
    }

    @GetMapping("/{name}")
    public ResponseEntity<ApiResponse> getProductsByName(@RequestParam("name") String productName) {
        List<Product> products = productService.getProductsByName(productName);
        return ResponseEntity.ok(new ApiResponse("Success", products));
    }
}
