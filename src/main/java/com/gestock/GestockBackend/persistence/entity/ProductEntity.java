package com.gestock.GestockBackend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import main.java.com.gestock.GestockBackend.entity.BusinessEntity;

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

    @Column(size = "100")
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
