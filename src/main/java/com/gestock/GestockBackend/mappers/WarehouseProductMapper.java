package com.gestock.GestockBackend.mappers;

import com.gestock.GestockBackend.model.dto.WarehouseProductRequestDto;
import com.gestock.GestockBackend.model.dto.WarehouseProductResponseDto;
import com.gestock.GestockBackend.model.entity.WarehouseProduct;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WarehouseProductMapper {

    @Mapping(source = "stock", target = "quantity")
    WarehouseProductResponseDto toResponseDto(WarehouseProduct warehouseProduct);

    List<WarehouseProductResponseDto> toResponseDtoList(List<WarehouseProduct> warehouseProducts);

    @Mapping(source = "quantity", target = "stock")
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    WarehouseProduct toEntity(WarehouseProductRequestDto warehouseProductRequestDto);

    @Mapping(source = "quantity", target = "stock")
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    void updateEntityFromDto(WarehouseProductRequestDto warehouseProductRequestDto, @MappingTarget WarehouseProduct warehouseProduct);
}
