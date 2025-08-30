package com.gestock.GestockBackend.persistence.repository;

import com.gestock.GestockBackend.persistence.entity.InventoryItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItemEntity, Long> {
}