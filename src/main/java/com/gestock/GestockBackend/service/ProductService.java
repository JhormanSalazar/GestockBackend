package com.gestock.GestockBackend.service;

import com.gestock.GestockBackend.mappers.ProductMapper;
import com.gestock.GestockBackend.model.entity.Product;
import com.gestock.GestockBackend.model.dto.ProductResponseDto;
import com.gestock.GestockBackend.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
        return productMapper.toResponseDto(product);
    }

    public List<ProductResponseDto> getAllProducts() {
        return productMapper.toResponseDtoList(productRepository.findAll());
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}

