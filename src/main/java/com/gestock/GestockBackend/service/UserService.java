package com.gestock.GestockBackend.service;

import com.gestock.GestockBackend.mappers.UserMapper;
import com.gestock.GestockBackend.model.entity.User;
import com.gestock.GestockBackend.model.dto.UserResponseDto;
import com.gestock.GestockBackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;


    // Crear o actualizar usuario
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Obtener usuario por ID
    public UserResponseDto getUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        User user = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        return userMapper.toResponseDto(user);
    }

    // Obtener todos los usuarios
    public List<UserResponseDto> getAllUsers() {
        return userMapper.toResponseDtoList(userRepository.findAll());
    }

    // Eliminar usuario por ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

