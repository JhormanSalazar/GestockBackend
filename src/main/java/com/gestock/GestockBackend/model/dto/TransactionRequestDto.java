package com.gestock.GestockBackend.model.dto;

import com.gestock.GestockBackend.model.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDto {
    private TransactionType type;
    private Integer quantity;
    private String description;
    private Long productId;
    private Long warehouseId;
}
