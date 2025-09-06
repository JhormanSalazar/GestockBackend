package com.gestock.GestockBackend.persistence.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "businesses")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessEntity {
    
    @Id
    @GeneratedValue
    @Column(name = "business_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
