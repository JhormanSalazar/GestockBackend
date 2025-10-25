package com.gestock.GestockBackend.service;

import com.gestock.GestockBackend.mappers.WarehouseMapper;
import com.gestock.GestockBackend.model.dto.WarehouseRequestDto;
import com.gestock.GestockBackend.model.dto.WarehouseResponseDto;
import com.gestock.GestockBackend.model.entity.Business;
import com.gestock.GestockBackend.model.entity.Warehouse;
import com.gestock.GestockBackend.repository.BusinessRepository;
import com.gestock.GestockBackend.repository.WarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final BusinessRepository businessRepository;
    private final Logger logger = LoggerFactory.getLogger(WarehouseService.class);

    // Obtiene un almacen por su ID
    public WarehouseResponseDto getWarehouseById(Long id) {
        return warehouseMapper.toResponseDto(warehouseRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Warehouse not found with ID: " + id)));
    }

    // Obtiene todos los almacenes
    public List<WarehouseResponseDto> getAllWarehouses() {
        return warehouseMapper.toResponseDtoList(warehouseRepository.findAll());
    }

    // Obtiene todos los almacenes asociados a un negocio por su ID
    public List<WarehouseResponseDto> getAllWarehousesByBusinessId(Long businessId) {
        if (businessId == null) {
            throw new IllegalArgumentException("Business ID cannot be null");
        }
        return warehouseMapper.toResponseDtoList(warehouseRepository.findByBusinessId(businessId));
    }

    // Crea un almacen y lo asocia a un negocio existente
    @Transactional
    public WarehouseResponseDto createWarehouse(WarehouseRequestDto requestDto) {
        if (requestDto.getBusinessId() == null) {
            throw new IllegalArgumentException("Business ID cannot be null");
        }

        Warehouse warehouse = warehouseMapper.toEntityFromRequest(requestDto);

    Business business = businessRepository.findById(requestDto.getBusinessId())
        .orElseThrow(() -> new EntityNotFoundException("Business not found with ID: " + requestDto.getBusinessId()));

    // Asignar el negocio a la entidad antes de persistir
    warehouse.setBusiness(business);
    logger.debug("Creating warehouse for business id {}: {}", business.getId(), warehouse.getName());

    return warehouseMapper.toResponseDto(warehouseRepository.save(warehouse));
    }

    // Actualiza un almacen existente
    @Transactional
    public WarehouseResponseDto updateWarehouse(Long id, WarehouseRequestDto requestDto) {
        Warehouse existingWarehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + id));

        if (requestDto.getName() != null) {
            existingWarehouse.setName(requestDto.getName());
        }
        if (requestDto.getAddress() != null) {
            existingWarehouse.setAddress(requestDto.getAddress());
        }
        if (requestDto.getMaxCapacity() != null) {
            existingWarehouse.setMaxCapacity(requestDto.getMaxCapacity());
        }

        return warehouseMapper.toResponseDto(warehouseRepository.save(existingWarehouse));
    }

    @Transactional
    public void deleteWarehouse(Long id) {
        warehouseRepository.deleteById(id);
    }
}
