package com.gestock.GestockBackend.service;

import com.gestock.GestockBackend.mappers.UserMapper;
import com.gestock.GestockBackend.model.dto.UserCreateDto;
import com.gestock.GestockBackend.model.entity.Business;
import com.gestock.GestockBackend.model.entity.Role;
import com.gestock.GestockBackend.model.entity.User;
import com.gestock.GestockBackend.model.dto.UserResponseDto;
import com.gestock.GestockBackend.repository.BusinessRepository;
import com.gestock.GestockBackend.repository.RoleRepository;
import com.gestock.GestockBackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


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
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    // Obtener usuarios por businessId
    public List<UserResponseDto> getUsersByBusinessId(Long businessId) {
        if (businessId == null) {
            throw new IllegalArgumentException("El businessId no puede ser nulo");
        }

        List<User> users = userRepository.findAll().stream()
                .filter(user -> user.getBusiness() != null && user.getBusiness().getId().equals(businessId))
                .collect(Collectors.toList());

        return userMapper.toResponseDtoList(users);
    }

    // Crear un nuevo usuario
    @Transactional
    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        // Validaciones
        if (userCreateDto.getEmail() == null || userCreateDto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (userCreateDto.getPassword() == null || userCreateDto.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        if (userCreateDto.getBusinessId() == null) {
            throw new IllegalArgumentException("El businessId es obligatorio");
        }
        if (userCreateDto.getRoleId() == null) {
            throw new IllegalArgumentException("El roleId es obligatorio");
        }

        // Verificar que el email no esté ya registrado
        if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Buscar el negocio
        Business business = businessRepository.findById(userCreateDto.getBusinessId())
                .orElseThrow(() -> new EntityNotFoundException("Negocio no encontrado con ID: " + userCreateDto.getBusinessId()));

        // Buscar el rol
        Role role = roleRepository.findById(userCreateDto.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + userCreateDto.getRoleId()));

        // Crear el usuario
        User newUser = new User();
        newUser.setEmail(userCreateDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        newUser.setBusiness(business);
        newUser.setRole(role);

        User savedUser = userRepository.save(newUser);
        return userMapper.toResponseDto(savedUser);
    }

    // Actualizar un usuario existente
    @Transactional
    public UserResponseDto updateUser(Long id, UserCreateDto userUpdateDto) {
        // Buscar el usuario existente
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        // Actualizar email si se proporciona y es diferente
        if (userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().trim().isEmpty()) {
            // Verificar que el email no esté ya en uso por otro usuario
            if (!existingUser.getEmail().equals(userUpdateDto.getEmail()) &&
                    userRepository.existsByEmail(userUpdateDto.getEmail())) {
                throw new IllegalArgumentException("El email ya está registrado por otro usuario");
            }
            existingUser.setEmail(userUpdateDto.getEmail());
        }

        // Actualizar contraseña si se proporciona
        if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        }

        // Actualizar negocio si se proporciona
        if (userUpdateDto.getBusinessId() != null) {
            Business business = businessRepository.findById(userUpdateDto.getBusinessId())
                    .orElseThrow(() -> new EntityNotFoundException("Negocio no encontrado con ID: " + userUpdateDto.getBusinessId()));
            existingUser.setBusiness(business);
        }

        // Actualizar rol si se proporciona
        if (userUpdateDto.getRoleId() != null) {
            Role role = roleRepository.findById(userUpdateDto.getRoleId())
                    .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + userUpdateDto.getRoleId()));
            existingUser.setRole(role);
        }

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toResponseDto(updatedUser);
    }
}

