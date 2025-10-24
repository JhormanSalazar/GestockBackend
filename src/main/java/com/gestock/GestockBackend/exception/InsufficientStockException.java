package com.gestock.GestockBackend.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(Long productId, Long warehouseId, Integer requested, Integer available) {
        super(String.format("Stock insuficiente para el producto ID: %d en almacén ID: %d. Solicitado: %d, Disponible: %d",
                productId, warehouseId, requested, available));
    }
}
