package com.gestock.GestockBackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "businesses")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    // Relación One-to-Many con Almacen (un negocio puede tener muchos almacenes)
    // @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Warehouse> warehouses;

    // Relación One-to-Many con Usuario (un negocio puede tener muchos usuarios)
    // @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<User> users;
}