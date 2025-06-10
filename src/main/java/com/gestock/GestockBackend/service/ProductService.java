package com.gestock.GestockBackend.service;

import com.gestock.GestockBackend.entity.ProductEntity;
import com.gestock.GestockBackend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public ProductEntity saveProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    public Optional<ProductEntity> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
