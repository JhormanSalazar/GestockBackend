package com.gestock.GestockBackend.repository;

import com.gestock.GestockBackend.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.warehouseProduct.warehouse.business.id = :businessId")
    List<Transaction> findByBusinessId(@Param("businessId") Long businessId);

    @Query("SELECT t FROM Transaction t WHERE t.warehouseProduct.warehouse.id = :warehouseId")
    List<Transaction> findByWarehouseId(@Param("warehouseId") Long warehouseId);

    @Query("SELECT t FROM Transaction t WHERE t.warehouseProduct.product.id = :productId")
    List<Transaction> findByProductId(@Param("productId") Long productId);
}
