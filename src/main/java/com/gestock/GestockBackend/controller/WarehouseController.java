package com.gestock.GestockBackend.controller;

import com.gestock.GestockBackend.model.dto.WarehouseRequestDto;
import com.gestock.GestockBackend.model.dto.WarehouseResponseDto;
import com.gestock.GestockBackend.security.GestockUserDetails;
import com.gestock.GestockBackend.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/warehouses")
public class WarehouseController {

    @Autowired
    private final WarehouseService warehouseService;

    // Helper method to get businessId from authenticated user
    private Long getBusinessIdFromAuth(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof GestockUserDetails) {
            return ((GestockUserDetails) authentication.getPrincipal()).getBusinessId();
        }
        throw new IllegalStateException("Usuario no autenticado o datos de negocio no disponibles");
    }

    // Helper method to check if user is ADMIN
    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("ROLE_ADMIN"));
    }

    @GetMapping
    public ResponseEntity<List<WarehouseResponseDto>> getAllWarehouses() {
        // Solo ADMIN puede acceder (configurado en SecurityConfig)
        return ResponseEntity.ok(warehouseService.getAllWarehouses());
    }

    @GetMapping("/{warehouseId}")
    public ResponseEntity<WarehouseResponseDto> getWarehouseById(@PathVariable("warehouseId") Long warehouseId) {
        return ResponseEntity.ok(warehouseService.getWarehouseById(warehouseId));
    }

    @GetMapping("/by-business/{businessId}")
    public ResponseEntity<List<WarehouseResponseDto>> getAllWarehousesByBusinessId(
            @PathVariable("businessId") Long businessId,
            Authentication authentication) {

        // Si no es ADMIN, validar que el businessId solicitado sea el del usuario
        if (!isAdmin(authentication)) {
            Long userBusinessId = getBusinessIdFromAuth(authentication);
            if (!userBusinessId.equals(businessId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        return ResponseEntity.ok(warehouseService.getAllWarehousesByBusinessId(businessId));
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<WarehouseResponseDto> createWarehouse(
            @RequestBody WarehouseRequestDto requestDto,
            Authentication authentication) {

        // Si no es ADMIN, validar que el businessId del warehouse sea el del usuario
        if (!isAdmin(authentication)) {
            Long userBusinessId = getBusinessIdFromAuth(authentication);
            if (!userBusinessId.equals(requestDto.getBusinessId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(null);
            }
        }

        return ResponseEntity.ok(warehouseService.createWarehouse(requestDto));
    }

    @PutMapping("/{warehouseId}")
    public ResponseEntity<WarehouseResponseDto> updateWarehouse(
        @PathVariable("warehouseId") Long warehouseId,
            @RequestBody WarehouseRequestDto requestDto,
            Authentication authentication) {

        // Obtener el warehouse existente para validar el businessId
        WarehouseResponseDto existingWarehouse = warehouseService.getWarehouseById(warehouseId);

        // Si no es ADMIN, validar que el warehouse pertenezca al negocio del usuario
        if (!isAdmin(authentication)) {
            Long userBusinessId = getBusinessIdFromAuth(authentication);
            if (!userBusinessId.equals(existingWarehouse.getBusinessId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        return ResponseEntity.ok(warehouseService.updateWarehouse(warehouseId, requestDto));
    }

    @DeleteMapping("/{warehouseId}")
    public ResponseEntity<Void> deleteWarehouse(
        @PathVariable("warehouseId") Long warehouseId,
            Authentication authentication) {

        // Obtener el warehouse para validar el businessId
        WarehouseResponseDto warehouse = warehouseService.getWarehouseById(warehouseId);

        // Si no es ADMIN, validar que el warehouse pertenezca al negocio del usuario
        if (!isAdmin(authentication)) {
            Long userBusinessId = getBusinessIdFromAuth(authentication);
            if (!userBusinessId.equals(warehouse.getBusinessId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        warehouseService.deleteWarehouse(warehouseId);
        return ResponseEntity.noContent().build();
    }
 }
