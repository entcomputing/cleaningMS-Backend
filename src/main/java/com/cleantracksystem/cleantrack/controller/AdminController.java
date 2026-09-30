package com.cleantracksystem.cleantrack.controller;

import com.cleantracksystem.cleantrack.model.Admin;
import com.cleantracksystem.cleantrack.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public List<Admin> getAll() {
        return adminService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getById(@PathVariable UUID id) {
        return adminService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // No POST here: creating an admin requires hashing a password, which
    // this entity-bound endpoint can't do safely - use POST /api/auth/admins
    // (bootstrap uses POST /api/auth/register) instead.

    @PutMapping("/{id}")
    public ResponseEntity<Admin> update(@PathVariable UUID id, @Valid @RequestBody Admin request) {
        return adminService.findById(id)
                .map(existing -> {
                    existing.setFullName(request.getFullName());
                    existing.setEmail(request.getEmail());
                    existing.setActive(request.isActive());
                    return ResponseEntity.ok(adminService.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!adminService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        adminService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
