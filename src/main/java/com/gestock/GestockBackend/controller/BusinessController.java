package com.gestock.GestockBackend.controller;

import com.gestock.GestockBackend.model.dto.BusinessResponseDto;
import com.gestock.GestockBackend.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/businesses")
public class BusinessController {

    @Autowired
    private final BusinessService businessService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BusinessResponseDto>> getAllBusinesses() {
        return ResponseEntity.ok(businessService.getAllBusinesses());
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<BusinessResponseDto> getBusinessById(@PathVariable Long businessId) {
        return ResponseEntity.ok(businessService.getBusinessById(businessId));
    }
}
