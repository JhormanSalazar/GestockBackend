package com.gestock.GestockBackend.controller;

import com.gestock.GestockBackend.model.dto.TransactionRequestDto;
import com.gestock.GestockBackend.model.dto.TransactionResponseDto;
import com.gestock.GestockBackend.security.GestockUserDetails;
import com.gestock.GestockBackend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    // Obtiene todas las transacciones del negocio del usuario autenticado
    @GetMapping("/by-business")
    public ResponseEntity<List<TransactionResponseDto>> getAllByBusiness(Authentication authentication) {
        Long businessId = getBusinessIdFromAuth(authentication);
        return ResponseEntity.ok(transactionService.getTransactionsByBusinessId(businessId));
    }

    // Obtiene transacciones por warehouse
    @GetMapping("/by-warehouse/{warehouseId}")
    public ResponseEntity<List<TransactionResponseDto>> getByWarehouse(
            @PathVariable Long warehouseId,
            Authentication authentication) {
        Long businessId = getBusinessIdFromAuth(authentication);
        return ResponseEntity.ok(transactionService.getTransactionsByWarehouseId(warehouseId, businessId));
    }

    // Obtiene transacciones por producto
    @GetMapping("/by-product/{productId}")
    public ResponseEntity<List<TransactionResponseDto>> getByProduct(
            @PathVariable Long productId,
            Authentication authentication) {
        Long businessId = getBusinessIdFromAuth(authentication);
        return ResponseEntity.ok(transactionService.getTransactionsByProductId(productId, businessId));
    }

    // Obtiene una transacción por ID
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getById(
            @PathVariable Long id,
            Authentication authentication) {
        Long businessId = getBusinessIdFromAuth(authentication);
        return ResponseEntity.ok(transactionService.getTransactionById(id, businessId));
    }

    // Crea una nueva transacción (ENTRADA o SALIDA de stock)
    @PostMapping
    public ResponseEntity<TransactionResponseDto> create(@RequestBody TransactionRequestDto requestDto) {
        TransactionResponseDto response = transactionService.createTransaction(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Elimina una transacción por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication) {
        Long businessId = getBusinessIdFromAuth(authentication);
        transactionService.deleteTransaction(id, businessId);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para extraer businessId del usuario autenticado
    private Long getBusinessIdFromAuth(Authentication authentication) {
        GestockUserDetails userDetails = (GestockUserDetails) authentication.getPrincipal();
        return userDetails.getBusinessId();
    }
}

