package com.cleantracksystem.cleantrack.service;

import com.cleantracksystem.cleantrack.dto.AuthResponse;
import com.cleantracksystem.cleantrack.dto.LoginRequest;
import com.cleantracksystem.cleantrack.dto.RegisterRequest;
import com.cleantracksystem.cleantrack.model.Admin;
import com.cleantracksystem.cleantrack.repository.AdminRepository;
import com.cleantracksystem.cleantrack.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    public boolean adminsExist() {
        return adminRepository.count() > 0;
    }

    /** One-time bootstrap: only succeeds while no admin exists yet. */
    public AuthResponse registerFirstAdmin(RegisterRequest request) {
        if (adminsExist()) {
            throw new IllegalStateException("An admin account already exists.");
        }
        Admin admin = createAdmin(request);
        return new AuthResponse(jwtService.issueToken(admin.getId()), admin);
    }

    /** An already-authenticated admin creates another one. Caller checks the requester is an active admin. */
    public Admin createAdmin(RegisterRequest request) {
        adminRepository.findByEmailIgnoreCase(request.email()).ifPresent(existing -> {
            throw new IllegalArgumentException("An admin with this email already exists.");
        });
        Admin admin = new Admin();
        admin.setFullName(request.fullName());
        admin.setEmail(request.email().toLowerCase());
        admin.setPasswordHash(passwordEncoder.encode(request.password()));
        admin.setActive(true);
        return adminRepository.save(admin);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Admin admin = adminRepository.findByEmailIgnoreCase(request.email())
                .filter(Admin::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        if (!passwordEncoder.matches(request.password(), admin.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }
        return new AuthResponse(jwtService.issueToken(admin.getId()), admin);
    }
}
