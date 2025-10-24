package com.gestock.GestockBackend.controller;

import com.gestock.GestockBackend.model.entity.Product;
import com.gestock.GestockBackend.model.dto.ProductResponseDto;
import com.gestock.GestockBackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        // Solo ADMIN o BUSINESS_OWNER (configurado en SecurityConfig)
        return ResponseEntity.ok(productService.saveProduct(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        // Solo ADMIN o BUSINESS_OWNER (configurado en SecurityConfig)
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
