package com.cleantracksystem.cleantrack.service;

import com.cleantracksystem.cleantrack.model.Area;
import com.cleantracksystem.cleantrack.model.Floor;
import com.cleantracksystem.cleantrack.model.Shift;
import com.cleantracksystem.cleantrack.repository.AreaRepository;
import com.cleantracksystem.cleantrack.repository.FloorRepository;
import com.cleantracksystem.cleantrack.repository.ShiftRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class AreaService {

    private final AreaRepository areaRepository;
    private final FloorRepository floorRepository;
    private final ShiftRepository shiftRepository;

    public AreaService(AreaRepository areaRepository, FloorRepository floorRepository, ShiftRepository shiftRepository) {
        this.areaRepository = areaRepository;
        this.floorRepository = floorRepository;
        this.shiftRepository = shiftRepository;
    }

    @Transactional(readOnly = true)
    public List<Area> findAll() {
        return areaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Area> findById(UUID id) {
        return areaRepository.findById(id);
    }

    public Area save(Area area) {
        Set<Floor> resolvedFloors = new HashSet<>();
        List<UUID> floorIds = area.getFloorIds() == null ? List.of() : area.getFloorIds();

        if (floorIds.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Area floorIds cannot contain null values.");
        }

        for (UUID floorId : floorIds) {
            Floor floor = floorRepository.findById(floorId)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown floor id: " + floorId));
            resolvedFloors.add(floor);
        }

        area.setFloors(resolvedFloors);

        Set<Shift> resolvedShifts = new HashSet<>();
        List<UUID> shiftIds = area.getShiftIds() == null ? List.of() : area.getShiftIds();

        if (shiftIds.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Area shiftIds cannot contain null values.");
        }

        for (UUID shiftId : shiftIds) {
            Shift shift = shiftRepository.findById(shiftId)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown shift id: " + shiftId));
            resolvedShifts.add(shift);
        }
        area.setShifts(resolvedShifts);

        return areaRepository.save(area);
    }

    public void deleteById(UUID id) {
        areaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return areaRepository.existsById(id);
    }
}
