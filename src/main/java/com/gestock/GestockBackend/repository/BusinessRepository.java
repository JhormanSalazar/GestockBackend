package com.gestock.GestockBackend.repository;

import com.gestock.GestockBackend.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessRepository extends JpaRepository<Business, Long> {
}
