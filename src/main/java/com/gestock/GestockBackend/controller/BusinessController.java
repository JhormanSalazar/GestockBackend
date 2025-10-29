package com.gestock.GestockBackend.controller;

import com.gestock.GestockBackend.model.dto.BusinessResponseDto;
import com.gestock.GestockBackend.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BusinessResponseDto> createBusiness(@RequestBody com.gestock.GestockBackend.model.dto.BusinessRequestDto requestDto) {
        BusinessResponseDto created = businessService.createBusiness(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{businessId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'BUSINESS_OWNER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteBusiness(@PathVariable Long businessId) {
        businessService.deleteBusiness(businessId);
        return ResponseEntity.noContent().build();
    }
}
