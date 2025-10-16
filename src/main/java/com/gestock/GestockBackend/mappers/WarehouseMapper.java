package com.gestock.GestockBackend.mappers;

import com.gestock.GestockBackend.model.dto.WarehouseRequestDto;
import com.gestock.GestockBackend.model.dto.WarehouseResponseDto;
import com.gestock.GestockBackend.model.entity.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {

    @Mappings({
        @Mapping(source = "business.id", target = "businessId"),
        @Mapping(source = "business.name", target = "businessName")
    })
    WarehouseResponseDto toResponseDto(Warehouse warehouse);

    List<WarehouseResponseDto> toResponseDtoList(List<Warehouse> warehouses);

    // Se ignora el mapeo del negocio al convertir de DTO a entidad para que se asigne manualmente en el servicio
    @Mapping(target = "business", ignore = true) // Ignorar el mapeo del negocio aquí
    Warehouse toEntity(WarehouseResponseDto warehouseResponseDto);

    Warehouse toEntityFromRequest(WarehouseRequestDto warehouseRequestDto);
}
