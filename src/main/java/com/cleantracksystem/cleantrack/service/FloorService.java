package com.cleantracksystem.cleantrack.service;

import com.cleantracksystem.cleantrack.model.Floor;
import com.cleantracksystem.cleantrack.repository.FloorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class FloorService {

    private final FloorRepository floorRepository;

    public FloorService(FloorRepository floorRepository) {
        this.floorRepository = floorRepository;
    }

    @Transactional(readOnly = true)
    public List<Floor> findAll() {
        return floorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Floor> findById(UUID id) {
        return floorRepository.findById(id);
    }

    public Floor save(Floor floor) {
        return floorRepository.save(floor);
    }

    public void deleteById(UUID id) {
        floorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return floorRepository.existsById(id);
    }
}
