package com.gestock.GestockBackend.mappers;

import com.gestock.GestockBackend.model.entity.Business;
import com.gestock.GestockBackend.model.dto.BusinessResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BusinessMapper {

    BusinessResponseDto toResponseDto(Business business);

    List<BusinessResponseDto> toResponseDtoList(List<Business> businesses);
}
