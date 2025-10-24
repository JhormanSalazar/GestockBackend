package com.gestock.GestockBackend.controller;

import com.gestock.GestockBackend.model.dto.WarehouseProductRequestDto;
import com.gestock.GestockBackend.model.dto.WarehouseProductResponseDto;
import com.gestock.GestockBackend.security.GestockUserDetails;
import com.gestock.GestockBackend.service.WarehouseProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouse-products")
@RequiredArgsConstructor
public class WarehouseProductController {
    private final WarehouseProductService warehouseProductService;

    // Obtiene todos los productos en almacenes del negocio del usuario autenticado
    @GetMapping("/by-business")
    public ResponseEntity<List<WarehouseProductResponseDto>> getAllByBusiness(Authentication authentication) {
        Long businessId = getBusinessIdFromAuth(authentication);
        List<WarehouseProductResponseDto> warehouseProducts = warehouseProductService.getWarehouseProductsByBusinessId(businessId);
        return ResponseEntity.ok(warehouseProducts);
    }

    // Obtiene todos los productos de un almacén específico
    @GetMapping("/by-warehouse/{warehouseId}")
    public ResponseEntity<List<WarehouseProductResponseDto>> getAllByWarehouse(
            @PathVariable Long warehouseId,
            Authentication authentication) {
        Long businessId = getBusinessIdFromAuth(authentication);
        List<WarehouseProductResponseDto> warehouseProducts = warehouseProductService.getWarehouseProductsByWarehouseId(warehouseId, businessId);
        return ResponseEntity.ok(warehouseProducts);
    }

    // Obtiene un producto específico en un almacén específico
    @GetMapping("/{productId}/{warehouseId}")
    public ResponseEntity<WarehouseProductResponseDto> getWarehouseProductById(
            @PathVariable Long productId,
            @PathVariable Long warehouseId,
            Authentication authentication) {
        Long businessId = getBusinessIdFromAuth(authentication);
        WarehouseProductResponseDto warehouseProduct = warehouseProductService.getWarehouseProductById(productId, warehouseId, businessId);
        return ResponseEntity.ok(warehouseProduct);
    }

    // Crea un nuevo producto en un almacén
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WarehouseProductResponseDto> createWarehouseProduct(
            @RequestBody WarehouseProductRequestDto request,
            Authentication authentication) {
        Long businessId = getBusinessIdFromAuth(authentication);
        WarehouseProductResponseDto createdWarehouseProduct = warehouseProductService.createWarehouseProduct(request, businessId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWarehouseProduct);
    }

    // Actualiza un producto en un almacén
    @PutMapping("/{productId}/{warehouseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WarehouseProductResponseDto> updateWarehouseProduct(
            @PathVariable Long productId,
            @PathVariable Long warehouseId,
            @RequestBody WarehouseProductRequestDto request,
            Authentication authentication) {
        Long businessId = getBusinessIdFromAuth(authentication);
        WarehouseProductResponseDto updatedWarehouseProduct = warehouseProductService.updateWarehouseProduct(productId, warehouseId, request, businessId);
        return ResponseEntity.ok(updatedWarehouseProduct);
    }

    // Elimina un producto de un almacén
    @DeleteMapping("/{productId}/{warehouseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteWarehouseProduct(
            @PathVariable Long productId,
            @PathVariable Long warehouseId,
            Authentication authentication) {
        Long businessId = getBusinessIdFromAuth(authentication);
        warehouseProductService.deleteWarehouseProduct(productId, warehouseId, businessId);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para extraer businessId del usuario autenticado
    private Long getBusinessIdFromAuth(Authentication authentication) {
        GestockUserDetails userDetails = (GestockUserDetails) authentication.getPrincipal();
        return userDetails.getBusinessId();
    }
}
