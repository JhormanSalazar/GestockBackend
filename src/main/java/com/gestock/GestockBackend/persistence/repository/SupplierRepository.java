package com.gestock.GestockBackend.persistence.repository;

import com.gestock.GestockBackend.persistence.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, Long> {
}