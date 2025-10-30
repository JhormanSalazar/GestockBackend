package com.gestock.GestockBackend.controller;

import com.gestock.GestockBackend.model.dto.ProductRequestDto;
import com.gestock.GestockBackend.model.dto.ProductResponseDto;
import com.gestock.GestockBackend.security.GestockUserDetails;
import com.gestock.GestockBackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    /**
     * Extrae el businessId del JWT token del usuario autenticado
     */
    private Long getBusinessIdFromAuth(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof GestockUserDetails) {
            return ((GestockUserDetails) authentication.getPrincipal()).getBusinessId();
        }
        throw new IllegalStateException("Usuario no autenticado o datos de negocio no disponibles");
    }

    /**
     * Verifica si el usuario tiene rol ADMIN
     */
    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(
            @PathVariable("id") Long id,
            Authentication authentication) {

        Long userBusinessId = getBusinessIdFromAuth(authentication);
        return ResponseEntity.ok(productService.getProductById(id, userBusinessId));
    }

    @GetMapping("/by-business/{businessId}")
    public ResponseEntity<List<ProductResponseDto>> getAllProductsByBusinessId(
            @PathVariable("businessId") Long businessId,
            Authentication authentication) {

        // Si no es ADMIN, validar que el businessId solicitado sea el del usuario
        if (!isAdmin(authentication)) {
            Long userBusinessId = getBusinessIdFromAuth(authentication);
            if (!userBusinessId.equals(businessId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        return ResponseEntity.ok(productService.getAllProductsByBusinessId(businessId));
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<ProductResponseDto> createProduct(
            @RequestBody ProductRequestDto requestDto,
            Authentication authentication) {

        // Si no es ADMIN, validar que el businessId del producto sea el del usuario
        if (!isAdmin(authentication)) {
            Long userBusinessId = getBusinessIdFromAuth(authentication);
            if (!userBusinessId.equals(requestDto.getBusinessId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        }

        return ResponseEntity.ok(productService.createProduct(requestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable("id") Long id,
            @RequestBody ProductRequestDto requestDto,
            Authentication authentication) {

        // Si no es ADMIN, validar que el businessId del producto sea el del usuario
        if (!isAdmin(authentication)) {
            Long userBusinessId = getBusinessIdFromAuth(authentication);
            if (requestDto.getBusinessId() != null && !userBusinessId.equals(requestDto.getBusinessId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        }

        Long userBusinessId = getBusinessIdFromAuth(authentication);
        return ResponseEntity.ok(productService.updateProduct(id, requestDto, userBusinessId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable("id") Long id,
            Authentication authentication) {

        Long userBusinessId = getBusinessIdFromAuth(authentication);
        productService.deleteProduct(id, userBusinessId);
        return ResponseEntity.noContent().build();
    }
}
