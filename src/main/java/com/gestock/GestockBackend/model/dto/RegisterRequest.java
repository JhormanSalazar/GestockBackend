package com.gestock.GestockBackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String businessName; // Nombre del negocio a crear
    private String email;
    private String password;
    // Rol por defecto será ADMIN para el primer usuario del negocio
}