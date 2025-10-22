package com.gestock.GestockBackend.repository;

import com.gestock.GestockBackend.model.entity.WarehouseProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseProductRepository extends JpaRepository<WarehouseProduct, Long> {
}

