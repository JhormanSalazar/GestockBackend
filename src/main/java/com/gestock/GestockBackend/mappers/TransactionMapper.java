package com.gestock.GestockBackend.mappers;

import com.gestock.GestockBackend.model.entity.Transaction;
import com.gestock.GestockBackend.model.dto.TransactionRequestDto;
import com.gestock.GestockBackend.model.dto.TransactionResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "createdBy.id", target = "userId")
    @Mapping(source = "createdBy.email", target = "userName")
    @Mapping(source = "warehouseProduct.product.id", target = "productId")
    @Mapping(source = "warehouseProduct.product.name", target = "productName")
    @Mapping(source = "warehouseProduct.warehouse.id", target = "warehouseId")
    @Mapping(source = "warehouseProduct.warehouse.name", target = "warehouseName")
    @Mapping(source = "warehouseProduct.warehouse.business.id", target = "businessId")
    TransactionResponseDto toResponseDto(Transaction transaction);

    List<TransactionResponseDto> toResponseDtoList(List<Transaction> transactions);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "warehouseProduct", ignore = true)
    Transaction toEntity(TransactionRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "warehouseProduct", ignore = true)
    void updateEntityFromDto(TransactionRequestDto requestDto, @MappingTarget Transaction transaction);
}
