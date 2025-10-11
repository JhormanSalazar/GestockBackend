package com.gestock.GestockBackend.service;

import com.gestock.GestockBackend.mappers.BusinessMapper;
import com.gestock.GestockBackend.model.dto.BusinessResponseDto;
import com.gestock.GestockBackend.repository.BusinessRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessService {
    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;

    public List<BusinessResponseDto> getAllBusinesses() {
        return businessMapper.toResponseDtoList(businessRepository.findAll());
    }

    public BusinessResponseDto getBusinessById(Long id) {
        return businessMapper.toResponseDto(businessRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Negocio no encontrado con ID: " + id)));
    }
}
