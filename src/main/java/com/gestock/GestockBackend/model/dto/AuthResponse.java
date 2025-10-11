package com.gestock.GestockBackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private Long id;
    private String email;
    private String jwt;
    private Long businessId; // Incluir el ID del negocio para el frontend
    private String role; // Incluir el rol del usuario
}