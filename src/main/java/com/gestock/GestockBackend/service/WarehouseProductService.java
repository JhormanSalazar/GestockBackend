package com.gestock.GestockBackend.service;

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

    public List<WarehouseProductResponseDto> getAllWarehouseProducts() {
        List<WarehouseProduct> warehouseProducts = warehouseProductRepository.findAll();
        return warehouseProductMapper.toResponseDtoList(warehouseProducts);
    }

    public WarehouseProductResponseDto getWarehouseProductById(Long productId, Long warehouseId) {
        WarehouseProduct warehouseProduct = warehouseProductRepository.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new RuntimeException("WarehouseProduct no encontrado para producto ID: " + productId + " y almacén ID: " + warehouseId));
        return warehouseProductMapper.toResponseDto(warehouseProduct);
    }

    @Transactional
    public WarehouseProductResponseDto createWarehouseProduct(WarehouseProductRequestDto requestDto) {
        // Validar que el warehouse existe
        Warehouse warehouse = warehouseRepository.findById(requestDto.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse no encontrado con ID: " + requestDto.getWarehouseId()));

        // Validar que el producto existe
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + requestDto.getProductId()));

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
    public WarehouseProductResponseDto updateWarehouseProduct(Long productId, Long warehouseId, WarehouseProductRequestDto requestDto) {
        // Buscar el WarehouseProduct existente
        WarehouseProduct warehouseProduct = warehouseProductRepository.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new RuntimeException("WarehouseProduct no encontrado para producto ID: " + productId + " y almacén ID: " + warehouseId));

        // Validar que el warehouse existe
        Warehouse warehouse = warehouseRepository.findById(requestDto.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse no encontrado con ID: " + requestDto.getWarehouseId()));

        // Validar que el producto existe
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + requestDto.getProductId()));

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
    public void deleteWarehouseProduct(Long productId, Long warehouseId) {
        // Verificar que existe antes de eliminar
        warehouseProductRepository.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new RuntimeException("WarehouseProduct no encontrado para producto ID: " + productId + " y almacén ID: " + warehouseId));

        // Eliminar usando el método optimizado
        warehouseProductRepository.deleteByProductIdAndWarehouseId(productId, warehouseId);
    }


}
