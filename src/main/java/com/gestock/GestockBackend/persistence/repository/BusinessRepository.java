package com.gestock.GestockBackend.persistence.repository;

import com.gestock.GestockBackend.persistence.entity.BusinessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends JpaRepository<BusinessEntity, Long> {
}