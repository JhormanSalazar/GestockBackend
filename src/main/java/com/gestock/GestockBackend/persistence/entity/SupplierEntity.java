package com.gestock.GestockBackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "contact_person")
    private String contactPerson;

    private String phone;

    private String email;

    private String address;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private BusinessEntity business;
}