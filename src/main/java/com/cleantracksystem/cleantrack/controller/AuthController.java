package com.cleantracksystem.cleantrack.controller;

import com.cleantracksystem.cleantrack.dto.LoginRequest;
import com.cleantracksystem.cleantrack.dto.RegisterRequest;
import com.cleantracksystem.cleantrack.model.Admin;
import com.cleantracksystem.cleantrack.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/admins-exist")
    public Map<String, Boolean> adminsExist() {
        return Map.of("exists", authService.adminsExist());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authService.registerFirstAdmin(request));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        }
    }

    // Admin-only: an already-signed-in admin creates another admin account.
    // SecurityConfig requires a valid token for this path; the authenticated
    // principal (set by JwtAuthFilter) is already a currently-active Admin.
    @PostMapping("/admins")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody RegisterRequest request) {
        try {
            Admin created = authService.createAdmin(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Admin> me(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof Admin admin)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(admin);
    }
}
