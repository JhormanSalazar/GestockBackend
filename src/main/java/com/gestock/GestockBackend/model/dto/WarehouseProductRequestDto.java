package com.gestock.GestockBackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseProductRequestDto {
    private Long productId;
    private Long warehouseId;
    private Integer quantity;
}
