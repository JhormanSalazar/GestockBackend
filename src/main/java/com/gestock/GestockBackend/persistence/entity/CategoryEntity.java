package com.gestock.GestockBackend.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "biseness_id")
    @JsonBackReference(value = "business-category")
    private BusinessEntity business;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference("category-product")
    private List<ProductEntity> products;
}
