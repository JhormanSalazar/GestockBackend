package com.gestock.GestockBackend.mappers;

import com.gestock.GestockBackend.model.entity.Product;
import com.gestock.GestockBackend.model.dto.ProductRequestDto;
import com.gestock.GestockBackend.model.dto.ProductResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponseDto toResponseDto(Product product);

    List<ProductResponseDto> toResponseDtoList(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "warehouseProducts", ignore = true)
    Product toEntity(ProductRequestDto productRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "warehouseProducts", ignore = true)
    void updateEntityFromDto(ProductRequestDto productRequestDto, @MappingTarget Product product);
}
