package com.gestock.GestockBackend.repository;

import com.gestock.GestockBackend.model.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    List<Warehouse> findByBusinessId(Long businessId);
}
