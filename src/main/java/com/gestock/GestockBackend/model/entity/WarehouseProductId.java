package com.gestock.GestockBackend.model.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
// Clase para la clave compuesta de WarehouseProduct
public class WarehouseProductId implements Serializable {
    private Long productId;
    private Long warehouseId;
}
