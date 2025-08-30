package com.gestock.GestockBackend.entity;

import jakarta.persistence.*;
import lombok.*;
import main.java.com.gestock.GestockBackend.entity.BusinessEntity;

@Entity
@Table(name = "warehouses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String location;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private BusinessEntity business;
}