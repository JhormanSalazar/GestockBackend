package com.gestock.GestockBackend.service;

import com.gestock.GestockBackend.entity.User;
import com.gestock.GestockBackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Crear o actualizar usuario
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Obtener usuario por ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Obtener todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Eliminar usuario por ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

