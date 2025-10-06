package com.gestock.GestockBackend.service;

import com.gestock.GestockBackend.dto.RegisterRequest;
import com.gestock.GestockBackend.entity.Business;
import com.gestock.GestockBackend.entity.Role;
import com.gestock.GestockBackend.entity.User;
import com.gestock.GestockBackend.repository.BusinessRepository;
import com.gestock.GestockBackend.repository.RoleRepository;
import com.gestock.GestockBackend.repository.UserRepository;
import com.gestock.GestockBackend.security.GestockUserDetailsService;
import com.gestock.GestockBackend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private GestockUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        // Crear el negocio primero
        Business newBusiness = new Business();
        newBusiness.setName(request.getBusinessName());
        businessRepository.save(newBusiness);

        // Buscar o crear el rol ADMIN (asumimos que existe o lo creamos si no)
        Role adminRol = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role newRol = new Role();
                    newRol.setName("ADMIN");
                    return roleRepository.save(newRol);
                });

        // Crear el usuario
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // ¡Cifrar la contraseña!
        newUser.setBusiness(newBusiness);
        newUser.setRole(adminRol);

        return userRepository.save(newUser);
    }

    public String authenticate(String email, String password) {
        // authenticationManager intentará autenticar usando GestockUserDetailsService y PasswordEncoder
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // Si la autenticación es exitosa, se obtiene el UserDetails (nuestro GestockUserDetails)
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generar el JWT
        return jwtUtil.generateToken(userDetails);
    }
}