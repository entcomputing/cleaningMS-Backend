package com.cleantracksystem.cleantrack.controller;

import com.cleantracksystem.cleantrack.model.CleaningItem;
import com.cleantracksystem.cleantrack.service.CleaningItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cleaning-items")
public class CleaningItemController {

    private final CleaningItemService cleaningItemService;

    public CleaningItemController(CleaningItemService cleaningItemService) {
        this.cleaningItemService = cleaningItemService;
    }

    @GetMapping
    public List<CleaningItem> getAll(@RequestParam(required = false) List<UUID> ids) {
        if (ids != null && !ids.isEmpty()) {
            return cleaningItemService.findByIds(ids);
        }
        return cleaningItemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CleaningItem> getById(@PathVariable UUID id) {
        return cleaningItemService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CleaningItem> create(@Valid @RequestBody CleaningItem cleaningItem) {
        CleaningItem saved = cleaningItemService.save(cleaningItem);
        return ResponseEntity.created(URI.create("/api/cleaning-items/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CleaningItem> update(@PathVariable UUID id, @Valid @RequestBody CleaningItem request) {
        return cleaningItemService.findById(id)
                .map(existing -> {
                    existing.setName(request.getName());
                    existing.setAreaIds(request.getAreaIds());
                    existing.setSortOrder(request.getSortOrder());
                    existing.setActive(request.isActive());
                    return ResponseEntity.ok(cleaningItemService.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!cleaningItemService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        cleaningItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
