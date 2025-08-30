
package com.gestock.GestockBackend.entity;

import com.gestock.GestockBackend.entity.ProductEntity;
import com.gestock.GestockBackend.entity.WarehouseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private WarehouseEntity warehouse;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}