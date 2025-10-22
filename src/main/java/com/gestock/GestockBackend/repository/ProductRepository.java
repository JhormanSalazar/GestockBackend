package com.gestock.GestockBackend.repository;

import com.gestock.GestockBackend.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

