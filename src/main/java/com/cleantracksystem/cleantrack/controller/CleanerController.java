package com.cleantracksystem.cleantrack.controller;

import com.cleantracksystem.cleantrack.model.Cleaner;
import com.cleantracksystem.cleantrack.service.CleanerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cleaners")
public class CleanerController {

    private final CleanerService cleanerService;

    public CleanerController(CleanerService cleanerService) {
        this.cleanerService = cleanerService;
    }

    @GetMapping
    public List<Cleaner> getAll() {
        return cleanerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cleaner> getById(@PathVariable UUID id) {
        return cleanerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cleaner> create(@Valid @RequestBody Cleaner cleaner) {
        Cleaner saved = cleanerService.save(cleaner);
        return ResponseEntity.created(URI.create("/api/cleaners/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cleaner> update(@PathVariable UUID id, @Valid @RequestBody Cleaner request) {
        return cleanerService.findById(id)
                .map(existing -> {
                    existing.setName(request.getName());
                    existing.setPhoneNumber(request.getPhoneNumber());
                    existing.setActive(request.isActive());
                    return ResponseEntity.ok(cleanerService.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!cleanerService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        cleanerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
