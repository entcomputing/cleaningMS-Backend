package com.cleantracksystem.cleantrack.controller;

import com.cleantracksystem.cleantrack.model.FloorAreaItem;
import com.cleantracksystem.cleantrack.service.FloorAreaItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/floor-area-items")
public class FloorAreaItemController {

    private final FloorAreaItemService floorAreaItemService;

    public FloorAreaItemController(FloorAreaItemService floorAreaItemService) {
        this.floorAreaItemService = floorAreaItemService;
    }

    @GetMapping
    public List<FloorAreaItem> getAll(
            @RequestParam(required = false) UUID floorId,
            @RequestParam(required = false) UUID areaId) {
        if (floorId != null && areaId != null) {
            return floorAreaItemService.findByFloorIdAndAreaId(floorId, areaId);
        }
        return floorAreaItemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FloorAreaItem> getById(@PathVariable UUID id) {
        return floorAreaItemService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FloorAreaItem> create(@Valid @RequestBody FloorAreaItem floorAreaItem) {
        FloorAreaItem saved = floorAreaItemService.save(floorAreaItem);
        return ResponseEntity.created(URI.create("/api/floor-area-items/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FloorAreaItem> update(@PathVariable UUID id, @Valid @RequestBody FloorAreaItem request) {
        return floorAreaItemService.findById(id)
                .map(existing -> {
                    existing.setFloorId(request.getFloorId());
                    existing.setAreaId(request.getAreaId());
                    existing.setItemId(request.getItemId());
                    existing.setSortOrder(request.getSortOrder());
                    existing.setActive(request.isActive());
                    return ResponseEntity.ok(floorAreaItemService.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!floorAreaItemService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        floorAreaItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
