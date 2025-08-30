package com.gestock.GestockBackend.persistence.repository;

import com.gestock.GestockBackend.persistence.entity.TransactionItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionItemRepository extends JpaRepository<TransactionItemEntity, Long> {
}