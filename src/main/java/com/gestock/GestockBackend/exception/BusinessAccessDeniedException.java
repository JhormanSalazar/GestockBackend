package com.gestock.GestockBackend.exception;

public class BusinessAccessDeniedException extends RuntimeException {
    public BusinessAccessDeniedException(String message) {
        super(message);
    }

    public BusinessAccessDeniedException(Long businessId) {
        super(String.format("Acceso denegado al negocio ID: %d", businessId));
    }
}
