package com.cleantracksystem.cleantrack.controller;

import com.cleantracksystem.cleantrack.model.CleaningRecord;
import com.cleantracksystem.cleantrack.service.CleaningRecordService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cleaning-records")
public class CleaningRecordController {

    private final CleaningRecordService cleaningRecordService;

    public CleaningRecordController(CleaningRecordService cleaningRecordService) {
        this.cleaningRecordService = cleaningRecordService;
    }

    @GetMapping
    public List<CleaningRecord> getAll() {
        return cleaningRecordService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CleaningRecord> getById(@PathVariable UUID id) {
        return cleaningRecordService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CleaningRecord> create(@Valid @RequestBody CleaningRecord cleaningRecord) {
        CleaningRecord saved = cleaningRecordService.save(cleaningRecord);
        return ResponseEntity.created(URI.create("/api/cleaning-records/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CleaningRecord> update(@PathVariable UUID id, @Valid @RequestBody CleaningRecord request) {
        return cleaningRecordService.findById(id)
                .map(existing -> {
                    existing.setCleaner(request.getCleaner());
                    existing.setCleanerName(request.getCleanerName());
                    existing.setSupervisorName(request.getSupervisorName());
                    existing.setShift(request.getShift());
                    existing.setShiftName(request.getShiftName());
                    existing.setRecordDate(request.getRecordDate());
                    existing.setTasksTotal(request.getTasksTotal());
                    existing.setTasksCompleted(request.getTasksCompleted());
                    existing.setIssuesCount(request.getIssuesCount());
                    existing.setInspectionStatus(request.getInspectionStatus());
                    existing.setInspectionNotes(request.getInspectionNotes());
                    existing.setInspectedAt(request.getInspectedAt());
                    existing.setSubmittedAt(request.getSubmittedAt());
                    return ResponseEntity.ok(cleaningRecordService.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!cleaningRecordService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        cleaningRecordService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
