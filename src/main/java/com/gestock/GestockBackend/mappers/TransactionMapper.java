package com.gestock.GestockBackend.mappers;

import com.gestock.GestockBackend.model.entity.Transaction;
import com.gestock.GestockBackend.model.dto.TransactionRequestDto;
import com.gestock.GestockBackend.model.dto.TransactionResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionResponseDto toResponseDto(Transaction transaction);

    List<TransactionResponseDto> toResponseDtoList(List<Transaction> transactions);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "warehouseProduct", ignore = true)
    Transaction toEntity(TransactionRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "warehouseProduct", ignore = true)
    void updateEntityFromDto(TransactionRequestDto requestDto, @MappingTarget Transaction transaction);
}
