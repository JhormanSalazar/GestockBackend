package com.gestock.GestockBackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column
    private Boolean availability;

    @Column
    private Double price;

    @Column(name = "product_stock")
    private Integer productStock;

    @Column(name = "product_image")
    private String productImage;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference("user-product")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonManagedReference("category-product")
    private CategoryEntity category;
}
