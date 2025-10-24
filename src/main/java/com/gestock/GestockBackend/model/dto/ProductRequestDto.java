package com.gestock.GestockBackend.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    private String name;
    private String sku;
    private Double price;
    private String description;
}
