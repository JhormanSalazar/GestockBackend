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
    private Long businessId;
    private String businessName;
    private String address;
    private Integer maxCapacity;
}
