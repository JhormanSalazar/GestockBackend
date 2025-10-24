package com.gestock.GestockBackend.repository;

import com.gestock.GestockBackend.model.entity.WarehouseProduct;
import com.gestock.GestockBackend.model.entity.WarehouseProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WarehouseProductRepository extends JpaRepository<WarehouseProduct, WarehouseProductId> {
    Optional<WarehouseProduct> findByProductIdAndWarehouseId(Long productId, Long warehouseId);

    @Modifying
    @Query("DELETE FROM WarehouseProduct wp WHERE wp.productId = :productId AND wp.warehouseId = :warehouseId")
    void deleteByProductIdAndWarehouseId(@Param("productId") Long productId, @Param("warehouseId") Long warehouseId);
}

