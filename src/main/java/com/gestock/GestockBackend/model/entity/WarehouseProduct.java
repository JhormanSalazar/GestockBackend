package com.gestock.GestockBackend.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(WarehouseProductId.class)
@Table(name = "warehouse_products")
public class WarehouseProduct {

    @Id
    @Column(name = "product_id")
    private Long productId;

    @Id
    @Column(name = "warehouse_id")
    private Long warehouseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(nullable = false)
    private Integer stock;

    // Constructor de conveniencia
    public WarehouseProduct(Product product, Warehouse warehouse, Integer stock) {
        this.product = product;
        this.warehouse = warehouse;
        this.productId = product.getId();
        this.warehouseId = warehouse.getId();
        this.stock = stock;
    }
}
