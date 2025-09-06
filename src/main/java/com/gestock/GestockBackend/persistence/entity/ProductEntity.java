package com.gestock.GestockBackend.persistence.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gestock.GestockBackend.persistence.entity.BusinessEntity;
import com.gestock.GestockBackend.persistence.entity.CategoryEntity;
import com.gestock.GestockBackend.persistence.entity.SupplierEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(length = 100)
    private String sku;

    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(name = "product_image")
    private String productImage;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference("category-product")
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private SupplierEntity supplier;
    
    @ManyToOne
    @JoinColumn(name = "business_id")
    private BusinessEntity business;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
