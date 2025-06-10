package com.gestock.GestockBackend.controller;

import com.gestock.GestockBackend.entity.UserEntity;
import com.gestock.GestockBackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // Inyección vía constructor
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Crear nuevo usuario
    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        UserEntity savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        Optional<UserEntity> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long id, @RequestBody UserEntity userDetails) {
        Optional<UserEntity> existingUserOpt = userService.getUserById(id);
        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserEntity existingUser = existingUserOpt.get();
        // Aquí debes mapear los campos que quieras actualizar
        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());
        // Añade otros campos según tu entidad UserEntity

        UserEntity updatedUser = userService.saveUser(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<UserEntity> existingUser = userService.getUserById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
