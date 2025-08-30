package com.gestock.GestockBackend.entity;

import jakarta.persistence.*;
import lombok.*;
import com.gestock.GestockBackend.entity.TransactionEntity;

@Entity
@Table(name = "transaction_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private TransactionEntity transaction;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;
}
