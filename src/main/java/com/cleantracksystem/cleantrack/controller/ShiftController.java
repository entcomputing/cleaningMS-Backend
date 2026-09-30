package com.cleantracksystem.cleantrack.controller;

import com.cleantracksystem.cleantrack.model.Shift;
import com.cleantracksystem.cleantrack.service.ShiftService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shifts")
public class ShiftController {

    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping
    public List<Shift> getAll() {
        return shiftService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shift> getById(@PathVariable UUID id) {
        return shiftService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Shift> create(@Valid @RequestBody Shift shift) {
        Shift saved = shiftService.save(shift);
        return ResponseEntity.created(URI.create("/api/shifts/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Shift> update(@PathVariable UUID id, @Valid @RequestBody Shift request) {
        return shiftService.findById(id)
                .map(existing -> {
                    existing.setName(request.getName());
                    existing.setStartTime(request.getStartTime());
                    existing.setEndTime(request.getEndTime());
                    existing.setDays(request.getDays());
                    existing.setActive(request.isActive());
                    return ResponseEntity.ok(shiftService.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!shiftService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        shiftService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
