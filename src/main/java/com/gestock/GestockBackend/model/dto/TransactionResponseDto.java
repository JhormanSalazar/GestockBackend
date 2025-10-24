package com.gestock.GestockBackend.model.dto;

import com.gestock.GestockBackend.model.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {
    private Long id;
    private TransactionType type;
    private Integer quantity;
    private String description;
    private LocalDateTime createdAt;
    private Long userId;
    private String userName;
    private Long productId;
    private String productName;
    private Long warehouseId;
    private String warehouseName;
    private Long businessId;
}
