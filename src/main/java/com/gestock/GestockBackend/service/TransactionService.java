package com.gestock.GestockBackend.service;

import com.gestock.GestockBackend.exception.BusinessAccessDeniedException;
import com.gestock.GestockBackend.exception.InsufficientStockException;
import com.gestock.GestockBackend.exception.ResourceNotFoundException;
import com.gestock.GestockBackend.mappers.TransactionMapper;
import com.gestock.GestockBackend.model.entity.*;
import com.gestock.GestockBackend.model.dto.TransactionRequestDto;
import com.gestock.GestockBackend.model.dto.TransactionResponseDto;
import com.gestock.GestockBackend.repository.TransactionRepository;
import com.gestock.GestockBackend.repository.UserRepository;
import com.gestock.GestockBackend.repository.WarehouseProductRepository;
import com.gestock.GestockBackend.repository.WarehouseRepository;
import com.gestock.GestockBackend.security.GestockUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final WarehouseProductRepository warehouseProductRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;

    @Transactional
    public TransactionResponseDto createTransaction(TransactionRequestDto requestDto) {
        // Obtener usuario autenticado
        User currentUser = getCurrentUser();
        Long businessId = currentUser.getBusiness().getId();

        // Validar que el warehouse pertenece al negocio del usuario
        Warehouse warehouse = warehouseRepository.findById(requestDto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", requestDto.getWarehouseId()));

        if (!warehouse.getBusiness().getId().equals(businessId)) {
            throw new BusinessAccessDeniedException("No tienes acceso a este almacén");
        }

        // Buscar el WarehouseProduct
        WarehouseProduct warehouseProduct = warehouseProductRepository
                .findByProductIdAndWarehouseId(requestDto.getProductId(), requestDto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado en el almacén especificado",
                        "productId/warehouseId",
                        requestDto.getProductId() + "/" + requestDto.getWarehouseId()
                ));

        // Actualizar stock según el tipo de transacción
        int currentStock = warehouseProduct.getStock();
        int newStock;

        if (requestDto.getType() == TransactionType.ENTRADA) {
            newStock = currentStock + requestDto.getQuantity();
        } else if (requestDto.getType() == TransactionType.SALIDA) {
            if (currentStock < requestDto.getQuantity()) {
                throw new InsufficientStockException(
                        requestDto.getProductId(),
                        requestDto.getWarehouseId(),
                        requestDto.getQuantity(),
                        currentStock
                );
            }
            newStock = currentStock - requestDto.getQuantity();
        } else {
            throw new IllegalArgumentException("Tipo de transacción inválido");
        }

        // Actualizar el stock
        warehouseProduct.setStock(newStock);
        warehouseProductRepository.save(warehouseProduct);

        // Crear la transacción
        Transaction transaction = transactionMapper.toEntity(requestDto);
        transaction.setWarehouseProduct(warehouseProduct);
        transaction.setCreatedBy(currentUser);

        Transaction saved = transactionRepository.save(transaction);
        return transactionMapper.toResponseDto(saved);
    }

    public TransactionResponseDto getTransactionById(Long id, Long businessId) {
        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));

        // Validar que pertenece al negocio del usuario
        if (!tx.getWarehouseProduct().getWarehouse().getBusiness().getId().equals(businessId)) {
            throw new BusinessAccessDeniedException("No tienes acceso a esta transacción");
        }

        return transactionMapper.toResponseDto(tx);
    }

    public List<TransactionResponseDto> getTransactionsByBusinessId(Long businessId) {
        return transactionMapper.toResponseDtoList(transactionRepository.findByBusinessId(businessId));
    }

    public List<TransactionResponseDto> getTransactionsByWarehouseId(Long warehouseId, Long businessId) {
        // Validar que el warehouse pertenece al negocio
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", warehouseId));

        if (!warehouse.getBusiness().getId().equals(businessId)) {
            throw new BusinessAccessDeniedException("No tienes acceso a este almacén");
        }

        return transactionMapper.toResponseDtoList(transactionRepository.findByWarehouseId(warehouseId));
    }

    public List<TransactionResponseDto> getTransactionsByProductId(Long productId, Long businessId) {
        List<Transaction> transactions = transactionRepository.findByProductId(productId);
        // Filtrar por businessId en el servicio para mayor seguridad
        List<Transaction> filteredTransactions = transactions.stream()
                .filter(t -> t.getWarehouseProduct().getWarehouse().getBusiness().getId().equals(businessId))
                .toList();

        return transactionMapper.toResponseDtoList(filteredTransactions);
    }

    @Transactional
    public void deleteTransaction(Long id, Long businessId) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));

        // Validar que pertenece al negocio del usuario
        if (!transaction.getWarehouseProduct().getWarehouse().getBusiness().getId().equals(businessId)) {
            throw new BusinessAccessDeniedException("No tienes acceso a esta transacción");
        }

        transactionRepository.deleteById(id);
    }

    // Método auxiliar para obtener el usuario autenticado
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        GestockUserDetails userDetails = (GestockUserDetails) authentication.getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userDetails.getId()));
    }
}
