package com.gestock.GestockBackend.controller;

import com.gestock.GestockBackend.model.dto.WarehouseProductRequestDto;
import com.gestock.GestockBackend.model.dto.WarehouseProductResponseDto;
import com.gestock.GestockBackend.service.WarehouseProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouse-products")
@RequiredArgsConstructor
public class WarehouseProductController {
    private final WarehouseProductService warehouseProductService;

    @GetMapping
    public ResponseEntity<List<WarehouseProductResponseDto>> getAllWarehouseProducts() {
        List<WarehouseProductResponseDto> warehouseProducts = warehouseProductService.getAllWarehouseProducts();
        return ResponseEntity.ok(warehouseProducts);
    }

    @GetMapping("/{productId}/{warehouseId}")
    public ResponseEntity<WarehouseProductResponseDto> getWarehouseProductById(
            @PathVariable Long productId,
            @PathVariable Long warehouseId) {
        WarehouseProductResponseDto warehouseProduct = warehouseProductService.getWarehouseProductById(productId, warehouseId);
        return ResponseEntity.ok(warehouseProduct);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WarehouseProductResponseDto> createWarehouseProduct(@RequestBody WarehouseProductRequestDto request) {
        WarehouseProductResponseDto createdWarehouseProduct = warehouseProductService.createWarehouseProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWarehouseProduct);
    }

    @PutMapping("/{productId}/{warehouseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WarehouseProductResponseDto> updateWarehouseProduct(
            @PathVariable Long productId,
            @PathVariable Long warehouseId,
            @RequestBody WarehouseProductRequestDto request) {
        WarehouseProductResponseDto updatedWarehouseProduct = warehouseProductService.updateWarehouseProduct(productId, warehouseId, request);
        return ResponseEntity.ok(updatedWarehouseProduct);
    }

    @DeleteMapping("/{productId}/{warehouseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteWarehouseProduct(
            @PathVariable Long productId,
            @PathVariable Long warehouseId) {
        warehouseProductService.deleteWarehouseProduct(productId, warehouseId);
        return ResponseEntity.noContent().build();
    }

}
