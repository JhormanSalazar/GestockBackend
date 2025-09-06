package com.gestock.GestockBackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    // Relación inversa con productos
    @ManyToOne
    @JoinColumn(name = "business_id")
    private BusinessEntity business;

    @Column(name = "is_owner", nullable = false)
    private boolean isOwner;

    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
