package com.gestock.GestockBackend.controller;

import com.gestock.GestockBackend.model.dto.WarehouseRequestDto;
import com.gestock.GestockBackend.model.dto.WarehouseResponseDto;
import com.gestock.GestockBackend.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/warehouses")
public class WarehouseController {

    @Autowired
    private final WarehouseService warehouseService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WarehouseResponseDto>> getAllWarehouses() {
        return ResponseEntity.ok(warehouseService.getAllWarehouses());
    }

    @GetMapping("/{warehouseId}")
    public ResponseEntity<WarehouseResponseDto> getWarehouseById(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(warehouseService.getWarehouseById(warehouseId));
    }

    @GetMapping("/by-business/{businessId}")
    public ResponseEntity<List<WarehouseResponseDto>> getAllWarehousesByBusinessId(@PathVariable Long businessId) {
        return ResponseEntity.ok(warehouseService.getAllWarehousesByBusinessId(businessId));
    }

    @PostMapping
    public ResponseEntity<WarehouseResponseDto> createWarehouse(@RequestBody WarehouseRequestDto requestDto) {
        return ResponseEntity.ok(warehouseService.createWarehouse(requestDto));
    }

    @PutMapping
    ResponseEntity<WarehouseResponseDto> updateWarehouse(@PathVariable Long warehouseId, @RequestBody WarehouseRequestDto requestDto) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(warehouseId, requestDto));
    }

    @DeleteMapping("/{warehouseId}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long warehouseId) {
        warehouseService.deleteWarehouse(warehouseId);
        return ResponseEntity.noContent().build();
    }
 }
