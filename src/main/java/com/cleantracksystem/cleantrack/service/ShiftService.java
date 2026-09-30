package com.cleantracksystem.cleantrack.service;

import com.cleantracksystem.cleantrack.model.Shift;
import com.cleantracksystem.cleantrack.repository.ShiftRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ShiftService {

    private final ShiftRepository shiftRepository;

    public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    @Transactional(readOnly = true)
    public List<Shift> findAll() {
        return shiftRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Shift> findById(UUID id) {
        return shiftRepository.findById(id);
    }

    public Shift save(Shift shift) {
        return shiftRepository.save(shift);
    }

    public void deleteById(UUID id) {
        shiftRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return shiftRepository.existsById(id);
    }
}
