package com.gestock.GestockBackend.model.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) // EAGER para que cargue el negocio y rol al obtener el usuario
    @JoinColumn(name = "business_id", nullable = false)
    @JsonBackReference(value = "business-users")
    private Business business;

    @ManyToOne(fetch = FetchType.EAGER) // EAGER para que cargue el negocio y rol al obtener el usuario
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password; // Esta contraseña se debe almacenar hasheada
}