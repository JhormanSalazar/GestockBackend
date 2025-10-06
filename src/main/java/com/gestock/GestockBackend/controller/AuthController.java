package com.gestock.GestockBackend.controller;

import com.gestock.GestockBackend.dto.AuthRequest;
import com.gestock.GestockBackend.dto.AuthResponse;
import com.gestock.GestockBackend.dto.RegisterRequest;
import com.gestock.GestockBackend.entity.User;
import com.gestock.GestockBackend.security.GestockUserDetails;
import com.gestock.GestockBackend.security.JwtUtil;
import com.gestock.GestockBackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserDetailsService userDetailsService; // Para cargar UserDetails después del login
    // Inyectamos userDetailsService en lugar de GestockUserDetailsService para mantener la abstracción

    @Autowired
    private JwtUtil jwtUtil; // Para extraer información del token después de la autenticación

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            User newUser = authService.register(request);
            // Después del registro, podríamos generar un token para el usuario o pedirle que haga login
            // Por simplicidad, por ahora solo confirmamos el registro.
            // Para una experiencia de usuario fluida, quizás quieras devolver directamente el AuthResponse
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario y negocio registrados exitosamente. Por favor, inicie sesión.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authenticationRequest) {
        try {
            // Autenticar y obtener el JWT
            String jwt = authService.authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

            // Una vez autenticado, cargar los detalles del usuario para obtener businessId y roles
            UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());


            Long userId = null;
            String userEmail = null;
            Long businessId = null;
            String role = null;

            if (userDetails instanceof GestockUserDetails) {
                userId = ((GestockUserDetails) userDetails).getId();
                userEmail = userDetails.getUsername();
                businessId = ((GestockUserDetails) userDetails).getBusinessId();
                // Asumiendo que el primer GrantedAuthority es el rol principal
                role = userDetails.getAuthorities().stream().findFirst().map(a -> a.getAuthority().replace("ROLE_", "")).orElse(null);
            }

            return ResponseEntity.ok(new AuthResponse(userId, userEmail, jwt, businessId, role));

        } catch (Exception e) {
            // Manejar errores de autenticación (ej. credenciales inválidas)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas: " + e.getMessage());
        }
    }
}