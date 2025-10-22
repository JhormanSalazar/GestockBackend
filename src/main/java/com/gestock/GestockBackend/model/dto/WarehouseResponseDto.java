package com.gestock.GestockBackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseResponseDto {
    private Long id;
    private String name;
    private Long businessId; // Incluir el ID del negocio para el frontend
    private Long businessName;
    private String address;
}
