package com.gestock.GestockBackend.service;

import com.gestock.GestockBackend.mappers.ProductMapper;
import com.gestock.GestockBackend.model.entity.Business;
import com.gestock.GestockBackend.model.entity.Product;
import com.gestock.GestockBackend.model.dto.ProductRequestDto;
import com.gestock.GestockBackend.model.dto.ProductResponseDto;
import com.gestock.GestockBackend.repository.BusinessRepository;
import com.gestock.GestockBackend.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final BusinessRepository businessRepository;

    public List<ProductResponseDto> getAllProductsByBusinessId(Long businessId) {
        if (businessId == null) {
            throw new IllegalArgumentException("Business ID cannot be null");
        }
        return productMapper.toResponseDtoList(productRepository.findByBusinessId(businessId));
    }

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        if (requestDto.getBusinessId() == null) {
            throw new IllegalArgumentException("Business ID cannot be null");
        }

        Product product = productMapper.toEntity(requestDto);

        Business business = businessRepository.findById(requestDto.getBusinessId())
                .orElseThrow(() -> new EntityNotFoundException("Business not found with ID: " + requestDto.getBusinessId()));

        product.setBusiness(business);
        logger.debug("Creating product for business id {}: {}", business.getId(), product.getName());

        return productMapper.toResponseDto(productRepository.save(product));
    }

    public ProductResponseDto getProductById(Long id, Long businessId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));

        // Validar que el producto pertenece al negocio
        if (!product.getBusiness().getId().equals(businessId)) {
            throw new IllegalArgumentException("Product does not belong to the specified business");
        }

        return productMapper.toResponseDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto, Long businessId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));

        // Validar que el producto pertenece al negocio
        if (!product.getBusiness().getId().equals(businessId)) {
            throw new IllegalArgumentException("Product does not belong to the specified business");
        }

        productMapper.updateEntityFromDto(requestDto, product);
        logger.debug("Updating product id {}: {}", id, product.getName());

        return productMapper.toResponseDto(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Long id, Long businessId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));

        // Validar que el producto pertenece al negocio
        if (!product.getBusiness().getId().equals(businessId)) {
            throw new IllegalArgumentException("Product does not belong to the specified business");
        }

        logger.debug("Deleting product id {}: {}", id, product.getName());
        productRepository.deleteById(id);
    }
}

