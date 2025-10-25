package com.gestock.GestockBackend.config;

import com.gestock.GestockBackend.security.GestockUserDetailsService;
import com.gestock.GestockBackend.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

import static com.gestock.GestockBackend.config.SecurityConstants.*;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private GestockUserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para APIs REST sin sesiones
                .cors(withDefaults()) // Habilitar CORS con configuración personalizada
                .authorizeHttpRequests(authRules -> authRules
                        // ==================== AUTH ENDPOINTS (Públicos) ====================
                        .requestMatchers(AUTH_REGISTER, AUTH_LOGIN).permitAll()

                        // ==================== H2 CONSOLE (Solo Development) ====================
                        .requestMatchers(H2_CONSOLE).permitAll()

                        // ==================== BUSINESS ENDPOINTS ====================
                        .requestMatchers(HttpMethod.GET, BUSINESSES).hasRole(ROLE_ADMIN)
                        .requestMatchers(HttpMethod.GET, BUSINESSES_ID).authenticated()

                        // ==================== WAREHOUSE ENDPOINTS ====================
                        // GET all warehouses - Solo ADMIN
                        .requestMatchers(HttpMethod.GET, WAREHOUSES).hasRole(ROLE_ADMIN)
                        // GET warehouses by business - ADMIN o BUSINESS_OWNER (validación adicional en controller)
                        .requestMatchers(HttpMethod.GET, WAREHOUSES_BY_BUSINESS).hasAnyRole(ROLE_ADMIN, ROLE_BUSINESS_OWNER)
                        // GET warehouse by ID - Cualquier usuario autenticado
                        .requestMatchers(HttpMethod.GET, WAREHOUSES_ID).authenticated()
                        // POST, PUT, DELETE warehouses - ADMIN o BUSINESS_OWNER
                        .requestMatchers(HttpMethod.POST, WAREHOUSES).hasAnyRole(ROLE_ADMIN, ROLE_BUSINESS_OWNER)
                        .requestMatchers(HttpMethod.PUT, WAREHOUSES_ID).hasAnyRole(ROLE_ADMIN, ROLE_BUSINESS_OWNER)
                        .requestMatchers(HttpMethod.DELETE, WAREHOUSES_ID).hasAnyRole(ROLE_ADMIN, ROLE_BUSINESS_OWNER)

                        // ==================== PRODUCT ENDPOINTS ====================
                        // GET products - Cualquier usuario autenticado
                        .requestMatchers(HttpMethod.GET, PRODUCTS).authenticated()
                        .requestMatchers(HttpMethod.GET, PRODUCTS_ID).authenticated()
                        // POST, DELETE products - ADMIN o BUSINESS_OWNER
                        .requestMatchers(HttpMethod.POST, PRODUCTS).hasAnyRole(ROLE_ADMIN, ROLE_BUSINESS_OWNER)
                        .requestMatchers(HttpMethod.DELETE, PRODUCTS_ID).hasAnyRole(ROLE_ADMIN, ROLE_BUSINESS_OWNER)

                        // ==================== USER ENDPOINTS ====================
                        // GET users - Solo ADMIN puede ver todos los usuarios
                        .requestMatchers(HttpMethod.GET, USERS).hasRole(ROLE_ADMIN)
                        .requestMatchers(HttpMethod.GET, USERS_ID).hasRole(ROLE_ADMIN)
                        // GET users by business - ADMIN o BUSINESS_OWNER (validación adicional en controller)
                        .requestMatchers(HttpMethod.GET, USERS_BY_BUSINESS).hasAnyRole(ROLE_ADMIN, ROLE_BUSINESS_OWNER)
                        // POST, PUT, DELETE users - ADMIN o BUSINESS_OWNER
                        .requestMatchers(HttpMethod.POST, USERS).hasAnyRole(ROLE_ADMIN, ROLE_BUSINESS_OWNER)
                        .requestMatchers(HttpMethod.PUT, USERS_ID).hasAnyRole(ROLE_ADMIN, ROLE_BUSINESS_OWNER)
                        .requestMatchers(HttpMethod.DELETE, USERS_ID).hasAnyRole(ROLE_ADMIN, ROLE_BUSINESS_OWNER)

                        // ==================== WAREHOUSE-PRODUCT ENDPOINTS ====================
                        // GET warehouse-products - Cualquier usuario autenticado (filtrado por business en controller)
                        .requestMatchers(HttpMethod.GET, WAREHOUSE_PRODUCTS_BY_BUSINESS).authenticated()
                        .requestMatchers(HttpMethod.GET, WAREHOUSE_PRODUCTS_BY_WAREHOUSE).authenticated()
                        .requestMatchers(HttpMethod.GET, WAREHOUSE_PRODUCTS_ID).authenticated()
                        // POST warehouse-products - ADMIN o BUSINESS_OWNER
                        .requestMatchers(HttpMethod.POST, WAREHOUSE_PRODUCTS).hasAnyRole(ROLE_ADMIN, ROLE_BUSINESS_OWNER)
                        // PUT warehouse-products - Todos los roles autenticados (COLLABORATOR puede actualizar stock)
                        .requestMatchers(HttpMethod.PUT, WAREHOUSE_PRODUCTS_ID).authenticated()
                        // DELETE warehouse-products - ADMIN o BUSINESS_OWNER
                        .requestMatchers(HttpMethod.DELETE, WAREHOUSE_PRODUCTS_ID).hasAnyRole(ROLE_ADMIN, ROLE_BUSINESS_OWNER)

                        // ==================== TRANSACTION ENDPOINTS ====================
                        // GET transactions - Cualquier usuario autenticado (filtrado por business en controller)
                        .requestMatchers(HttpMethod.GET, TRANSACTIONS_BY_BUSINESS).authenticated()
                        .requestMatchers(HttpMethod.GET, TRANSACTIONS_BY_WAREHOUSE).authenticated()
                        .requestMatchers(HttpMethod.GET, TRANSACTIONS_BY_PRODUCT).authenticated()
                        .requestMatchers(HttpMethod.GET, TRANSACTIONS_ID).authenticated()
                        // POST transactions - Todos los roles (crear movimientos de stock)
                        .requestMatchers(HttpMethod.POST, TRANSACTIONS).authenticated()
                        // DELETE transactions - ADMIN o BUSINESS_OWNER
                        .requestMatchers(HttpMethod.DELETE, TRANSACTIONS_ID).hasAnyRole(ROLE_ADMIN, ROLE_BUSINESS_OWNER)

                        // ==================== CUALQUIER OTRA RUTA ====================
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No usar sesiones para JWT
                )
                .authenticationProvider(authenticationProvider()) // Usar nuestro proveedor de autenticación
                // Añadir el filtro JWT antes del filtro de autenticación de usuario/contraseña de Spring
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // Permitir frames de H2 Console (solo development)
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }

    // Configuración de CORS
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        // Permitir orígenes específicos para desarrollo
        // Frontend puede estar en puerto 3000 (Create React App) o 8080 (Vite)
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",  // React (Create React App)
                "http://localhost:8080",  // Vite (Frontend actual)
                "http://localhost:5173"   // Vite (puerto por defecto alternativo)
        ));

        // Permitir todos los headers y métodos
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Exponer headers de respuesta
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Aplicar configuración a todas las rutas
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}