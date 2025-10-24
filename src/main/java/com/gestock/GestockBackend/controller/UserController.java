package com.gestock.GestockBackend.controller;

import com.gestock.GestockBackend.model.dto.UserCreateDto;
import com.gestock.GestockBackend.model.dto.UserResponseDto;
import com.gestock.GestockBackend.security.GestockUserDetails;
import com.gestock.GestockBackend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService userService;

    // Helper method to get businessId from authenticated user
    private Long getBusinessIdFromAuth(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof GestockUserDetails) {
            return ((GestockUserDetails) authentication.getPrincipal()).getBusinessId();
        }
        throw new IllegalStateException("Usuario no autenticado o datos de negocio no disponibles");
    }

    // Helper method to check if user is ADMIN
    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("ROLE_ADMIN"));
    }

    // ==================== GET ENDPOINTS ====================

    /**
     * Obtiene todos los usuarios del sistema (solo ADMIN)
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        // Solo ADMIN puede ver todos los usuarios (configurado en SecurityConfig)
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Obtiene un usuario por ID (solo ADMIN)
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene todos los usuarios de un negocio específico
     * ADMIN: Puede ver usuarios de cualquier negocio
     * BUSINESS_OWNER: Solo puede ver usuarios de su propio negocio
     */
    @GetMapping("/by-business/{businessId}")
    public ResponseEntity<List<UserResponseDto>> getUsersByBusinessId(
            @PathVariable Long businessId,
            Authentication authentication) {

        try {
            // Si no es ADMIN, validar que el businessId solicitado sea el del usuario
            if (!isAdmin(authentication)) {
                Long userBusinessId = getBusinessIdFromAuth(authentication);
                if (!userBusinessId.equals(businessId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            }

            return ResponseEntity.ok(userService.getUsersByBusinessId(businessId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== POST ENDPOINTS ====================

    /**
     * Crea un nuevo usuario
     * ADMIN: Puede crear usuarios para cualquier negocio
     * BUSINESS_OWNER: Solo puede crear usuarios para su propio negocio
     */
    @PostMapping
    public ResponseEntity<?> createUser(
            @RequestBody UserCreateDto userCreateDto,
            Authentication authentication) {

        try {
            // Si no es ADMIN, validar que el businessId del nuevo usuario sea el del usuario autenticado
            if (!isAdmin(authentication)) {
                Long userBusinessId = getBusinessIdFromAuth(authentication);
                if (!userBusinessId.equals(userCreateDto.getBusinessId())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("No tiene permisos para crear usuarios en otro negocio");
                }
            }

            UserResponseDto createdUser = userService.createUser(userCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear usuario: " + e.getMessage());
        }
    }

    // ==================== PUT ENDPOINTS ====================

    /**
     * Actualiza un usuario existente
     * ADMIN: Puede actualizar cualquier usuario
     * BUSINESS_OWNER: Solo puede actualizar usuarios de su propio negocio
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody UserCreateDto userUpdateDto,
            Authentication authentication) {

        try {
            // Obtener el usuario existente para validar el businessId
            UserResponseDto existingUser = userService.getUserById(id);

            // Si no es ADMIN, validar que el usuario pertenezca al negocio del usuario autenticado
            if (!isAdmin(authentication)) {
                Long userBusinessId = getBusinessIdFromAuth(authentication);
                if (!userBusinessId.equals(existingUser.getBusinessId())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("No tiene permisos para actualizar usuarios de otro negocio");
                }
            }

            UserResponseDto updatedUser = userService.updateUser(id, userUpdateDto);
            return ResponseEntity.ok(updatedUser);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar usuario: " + e.getMessage());
        }
    }

    // ==================== DELETE ENDPOINTS ====================

    /**
     * Elimina un usuario
     * ADMIN: Puede eliminar cualquier usuario
     * BUSINESS_OWNER: Solo puede eliminar usuarios de su propio negocio
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id,
            Authentication authentication) {

        try {
            // Obtener el usuario para validar el businessId
            UserResponseDto user = userService.getUserById(id);

            // Si no es ADMIN, validar que el usuario pertenezca al negocio del usuario autenticado
            if (!isAdmin(authentication)) {
                Long userBusinessId = getBusinessIdFromAuth(authentication);
                if (!userBusinessId.equals(user.getBusinessId())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("No tiene permisos para eliminar usuarios de otro negocio");
                }
            }

            userService.deleteUser(id);
            return ResponseEntity.noContent().build();

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar usuario: " + e.getMessage());
        }
    }
}
