package com.cleantracksystem.cleantrack.controller;

import com.cleantracksystem.cleantrack.model.Area;
import com.cleantracksystem.cleantrack.service.AreaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/areas")
public class AreaController {

    private final AreaService areaService;

    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @GetMapping
    public List<Area> getAll() {
        return areaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Area> getById(@PathVariable UUID id) {
        return areaService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Area> create(@Valid @RequestBody Area area) {
        Area saved = areaService.save(area);
        return ResponseEntity.created(URI.create("/api/areas/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Area> update(@PathVariable UUID id, @Valid @RequestBody Area request) {
        return areaService.findById(id)
                .map(existing -> {
                    existing.setName(request.getName());
                    existing.setFloorIds(request.getFloorIds());
                    existing.setShiftIds(request.getShiftIds());
                    existing.setSortOrder(request.getSortOrder());
                    existing.setActive(request.isActive());
                    return ResponseEntity.ok(areaService.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!areaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        areaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
