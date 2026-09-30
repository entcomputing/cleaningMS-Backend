package com.cleantracksystem.cleantrack.service;

import com.cleantracksystem.cleantrack.model.Admin;
import com.cleantracksystem.cleantrack.repository.AdminRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Transactional(readOnly = true)
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Admin> findById(UUID id) {
        return adminRepository.findById(id);
    }

    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    public void deleteById(UUID id) {
        adminRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return adminRepository.existsById(id);
    }
}
