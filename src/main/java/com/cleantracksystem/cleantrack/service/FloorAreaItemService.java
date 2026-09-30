package com.cleantracksystem.cleantrack.service;

import com.cleantracksystem.cleantrack.model.FloorAreaItem;
import com.cleantracksystem.cleantrack.repository.FloorAreaItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class FloorAreaItemService {

    private final FloorAreaItemRepository floorAreaItemRepository;

    public FloorAreaItemService(FloorAreaItemRepository floorAreaItemRepository) {
        this.floorAreaItemRepository = floorAreaItemRepository;
    }

    @Transactional(readOnly = true)
    public List<FloorAreaItem> findAll() {
        return floorAreaItemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<FloorAreaItem> findById(UUID id) {
        return floorAreaItemRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<FloorAreaItem> findByFloorIdAndAreaId(UUID floorId, UUID areaId) {
        return floorAreaItemRepository.findByFloorIdAndAreaId(floorId, areaId);
    }

    public FloorAreaItem save(FloorAreaItem floorAreaItem) {
        if (floorAreaItem.getFloorId() == null) {
            throw new IllegalArgumentException("FloorAreaItem floor_id cannot be null.");
        }
        if (floorAreaItem.getAreaId() == null) {
            throw new IllegalArgumentException("FloorAreaItem area_id cannot be null.");
        }
        if (floorAreaItem.getItemId() == null) {
            throw new IllegalArgumentException("FloorAreaItem item_id cannot be null.");
        }
        return floorAreaItemRepository.save(floorAreaItem);
    }

    public void deleteById(UUID id) {
        floorAreaItemRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return floorAreaItemRepository.existsById(id);
    }
}
