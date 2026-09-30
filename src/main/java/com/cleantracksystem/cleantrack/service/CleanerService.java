package com.cleantracksystem.cleantrack.service;

import com.cleantracksystem.cleantrack.model.Cleaner;
import com.cleantracksystem.cleantrack.repository.CleanerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CleanerService {

    private final CleanerRepository cleanerRepository;

    public CleanerService(CleanerRepository cleanerRepository) {
        this.cleanerRepository = cleanerRepository;
    }

    @Transactional(readOnly = true)
    public List<Cleaner> findAll() {
        return cleanerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Cleaner> findById(UUID id) {
        return cleanerRepository.findById(id);
    }

    public Cleaner save(Cleaner cleaner) {
        return cleanerRepository.save(cleaner);
    }

    public void deleteById(UUID id) {
        cleanerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return cleanerRepository.existsById(id);
    }
}
