package com.gestock.GestockBackend.security;

import com.gestock.GestockBackend.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class GestockUserDetails implements UserDetails {

    @Getter
    private Long id;
    private String email;
    private String password;
    @Getter
    private Long businessId; // Necesario para el aislamiento de datos
    private Collection<? extends GrantedAuthority> authorities;

    public GestockUserDetails(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.businessId = user.getBusiness().getId();
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().toUpperCase()));
        // Importante: Spring Security espera roles con el prefijo "ROLE_"
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() { // Spring Security usa "username", nosotros usaremos "email"
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}