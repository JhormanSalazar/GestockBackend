package com.gestock.GestockBackend.mappers;

import com.gestock.GestockBackend.model.entity.User;
import com.gestock.GestockBackend.model.dto.UserCreateDto;
import com.gestock.GestockBackend.model.dto.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Mapear User entity a UserResponseDto
    @Mapping(source = "business.name", target = "businessName")
    @Mapping(source = "role.name", target = "roleName")
    UserResponseDto toResponseDto(User user);

    // Mapear lista de Users a lista de UserResponseDto
    List<UserResponseDto> toResponseDtoList(List<User> users);

    // Mapear UserCreateDto a User entity (sin mapear businessId y roleId directamente)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(UserCreateDto userCreateDto);

    // Método para actualizar User entity desde UserCreateDto (útil para updates)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateEntityFromDto(UserCreateDto userCreateDto, @MappingTarget User user);

}
