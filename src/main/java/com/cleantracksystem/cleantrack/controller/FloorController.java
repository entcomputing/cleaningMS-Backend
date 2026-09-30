package com.cleantracksystem.cleantrack.controller;

import com.cleantracksystem.cleantrack.model.Floor;
import com.cleantracksystem.cleantrack.service.FloorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/floors")
public class FloorController {

    private final FloorService floorService;

    public FloorController(FloorService floorService) {
        this.floorService = floorService;
    }

    @GetMapping
    public List<Floor> getAll() {
        return floorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Floor> getById(@PathVariable UUID id) {
        return floorService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Floor> create(@Valid @RequestBody Floor floor) {
        Floor saved = floorService.save(floor);
        return ResponseEntity.created(URI.create("/api/floors/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Floor> update(@PathVariable UUID id, @Valid @RequestBody Floor request) {
        return floorService.findById(id)
                .map(existing -> {
                    existing.setName(request.getName());
                    existing.setSortOrder(request.getSortOrder());
                    existing.setActive(request.isActive());
                    return ResponseEntity.ok(floorService.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!floorService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        floorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
