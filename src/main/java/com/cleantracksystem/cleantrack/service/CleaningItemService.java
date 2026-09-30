package com.cleantracksystem.cleantrack.service;

import com.cleantracksystem.cleantrack.model.CleaningItem;
import com.cleantracksystem.cleantrack.repository.AreaRepository;
import com.cleantracksystem.cleantrack.repository.CleaningItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CleaningItemService {

    private final CleaningItemRepository cleaningItemRepository;
    private final AreaRepository areaRepository;

    public CleaningItemService(CleaningItemRepository cleaningItemRepository, AreaRepository areaRepository) {
        this.cleaningItemRepository = cleaningItemRepository;
        this.areaRepository = areaRepository;
    }

    @Transactional(readOnly = true)
    public List<CleaningItem> findAll() {
        return cleaningItemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<CleaningItem> findById(UUID id) {
        return cleaningItemRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<CleaningItem> findByIds(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return cleaningItemRepository.findAllByIdIn(ids);
    }

    public CleaningItem save(CleaningItem cleaningItem) {
        // Legacy area-to-item attachments are no longer the source of truth; item
        // assignment is scoped to (floor, area, item) via floor_area_items. Keeping
        // the legacy area relation empty prevents the same item from being
        // inadvertently shared across floors when the UI or API still sends an old
        // area_ids payload.
        cleaningItem.setAreas(new HashSet<>());
        cleaningItem.setAreaIds(List.of());

        return cleaningItemRepository.save(cleaningItem);
    }

    public void deleteById(UUID id) {
        cleaningItemRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return cleaningItemRepository.existsById(id);
    }
}
