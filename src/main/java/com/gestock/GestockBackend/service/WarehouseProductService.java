package com.gestock.GestockBackend.service;

import com.gestock.GestockBackend.exception.BusinessAccessDeniedException;
import com.gestock.GestockBackend.exception.ResourceNotFoundException;
import com.gestock.GestockBackend.mappers.WarehouseProductMapper;
import com.gestock.GestockBackend.model.dto.WarehouseProductRequestDto;
import com.gestock.GestockBackend.model.dto.WarehouseProductResponseDto;
import com.gestock.GestockBackend.model.entity.Product;
import com.gestock.GestockBackend.model.entity.Warehouse;
import com.gestock.GestockBackend.model.entity.WarehouseProduct;
import com.gestock.GestockBackend.repository.ProductRepository;
import com.gestock.GestockBackend.repository.WarehouseProductRepository;
import com.gestock.GestockBackend.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseProductService {
    private final WarehouseProductRepository warehouseProductRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final WarehouseProductMapper warehouseProductMapper;

    public List<WarehouseProductResponseDto> getWarehouseProductsByBusinessId(Long businessId) {
        List<WarehouseProduct> warehouseProducts = warehouseProductRepository.findByBusinessId(businessId);
        return warehouseProductMapper.toResponseDtoList(warehouseProducts);
    }

    public List<WarehouseProductResponseDto> getWarehouseProductsByWarehouseId(Long warehouseId, Long businessId) {
        // Validar que el warehouse pertenece al negocio
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", warehouseId));

        if (!warehouse.getBusiness().getId().equals(businessId)) {
            throw new BusinessAccessDeniedException("No tienes acceso a este almacén");
        }

        List<WarehouseProduct> warehouseProducts = warehouseProductRepository.findByWarehouseId(warehouseId);
        return warehouseProductMapper.toResponseDtoList(warehouseProducts);
    }

    public WarehouseProductResponseDto getWarehouseProductById(Long productId, Long warehouseId, Long businessId) {
        WarehouseProduct warehouseProduct = warehouseProductRepository.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "WarehouseProduct no encontrado",
                        "productId/warehouseId",
                        productId + "/" + warehouseId));

        // Validar que pertenece al negocio del usuario
        if (!warehouseProduct.getWarehouse().getBusiness().getId().equals(businessId)) {
            throw new BusinessAccessDeniedException("No tienes acceso a este producto");
        }

        return warehouseProductMapper.toResponseDto(warehouseProduct);
    }

    @Transactional
    public WarehouseProductResponseDto createWarehouseProduct(WarehouseProductRequestDto requestDto, Long businessId) {
        // Validar que el warehouse existe
        Warehouse warehouse = warehouseRepository.findById(requestDto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", requestDto.getWarehouseId()));

        // Validar que el warehouse pertenece al negocio del usuario
        if (!warehouse.getBusiness().getId().equals(businessId)) {
            throw new BusinessAccessDeniedException("No tienes acceso a este almacén");
        }

        // Validar que el producto existe
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", requestDto.getProductId()));

        // Verificar que no exista ya la combinación
        warehouseProductRepository.findByProductIdAndWarehouseId(requestDto.getProductId(), requestDto.getWarehouseId())
                .ifPresent(wp -> {
                    throw new IllegalArgumentException("El producto ya existe en este almacén");
                });

        // Crear la entidad usando el mapper
        WarehouseProduct warehouseProduct = warehouseProductMapper.toEntity(requestDto);

        // Asignar las entidades resueltas
        warehouseProduct.setWarehouse(warehouse);
        warehouseProduct.setProduct(product);

        // Guardar y retornar como DTO
        WarehouseProduct savedWarehouseProduct = warehouseProductRepository.save(warehouseProduct);
        return warehouseProductMapper.toResponseDto(savedWarehouseProduct);
    }

    @Transactional
    public WarehouseProductResponseDto updateWarehouseProduct(Long productId, Long warehouseId, WarehouseProductRequestDto requestDto, Long businessId) {
        // Buscar el WarehouseProduct existente
        WarehouseProduct warehouseProduct = warehouseProductRepository.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "WarehouseProduct no encontrado",
                        "productId/warehouseId",
                        productId + "/" + warehouseId));

        // Validar que pertenece al negocio del usuario
        if (!warehouseProduct.getWarehouse().getBusiness().getId().equals(businessId)) {
            throw new BusinessAccessDeniedException("No tienes acceso a este producto");
        }

        // Validar que el warehouse existe
        Warehouse warehouse = warehouseRepository.findById(requestDto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", requestDto.getWarehouseId()));

        // Validar que el producto existe
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", requestDto.getProductId()));

        // Actualizar usando el mapper
        warehouseProductMapper.updateEntityFromDto(requestDto, warehouseProduct);

        // Asignar las entidades resueltas
        warehouseProduct.setWarehouse(warehouse);
        warehouseProduct.setProduct(product);

        // Guardar y retornar como DTO
        WarehouseProduct updatedWarehouseProduct = warehouseProductRepository.save(warehouseProduct);
        return warehouseProductMapper.toResponseDto(updatedWarehouseProduct);
    }

    @Transactional
    public void deleteWarehouseProduct(Long productId, Long warehouseId, Long businessId) {
        // Verificar que existe antes de eliminar
        WarehouseProduct warehouseProduct = warehouseProductRepository.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "WarehouseProduct no encontrado",
                        "productId/warehouseId",
                        productId + "/" + warehouseId));

        // Validar que pertenece al negocio del usuario
        if (!warehouseProduct.getWarehouse().getBusiness().getId().equals(businessId)) {
            throw new BusinessAccessDeniedException("No tienes acceso a este producto");
        }

        // Eliminar usando el método optimizado
        warehouseProductRepository.deleteByProductIdAndWarehouseId(productId, warehouseId);
    }
}
