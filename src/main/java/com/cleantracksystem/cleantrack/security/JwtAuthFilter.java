package com.cleantracksystem.cleantrack.security;

import com.cleantracksystem.cleantrack.model.Admin;
import com.cleantracksystem.cleantrack.repository.AdminRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AdminRepository adminRepository;

    public JwtAuthFilter(JwtService jwtService, AdminRepository adminRepository) {
        this.jwtService = jwtService;
        this.adminRepository = adminRepository;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring("Bearer ".length());
            Optional<UUID> adminId = jwtService.validateAndGetAdminId(token);
            if (adminId.isPresent()) {
                Optional<Admin> admin = adminRepository.findById(adminId.get());
                if (admin.isPresent() && admin.get().isActive()) {
                    var authentication = new UsernamePasswordAuthenticationToken(
                            admin.get(), null, List.of());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
