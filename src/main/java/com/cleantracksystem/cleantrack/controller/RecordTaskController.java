package com.cleantracksystem.cleantrack.controller;

import com.cleantracksystem.cleantrack.model.RecordTask;
import com.cleantracksystem.cleantrack.service.RecordTaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/record-tasks")
public class RecordTaskController {

    private final RecordTaskService recordTaskService;

    public RecordTaskController(RecordTaskService recordTaskService) {
        this.recordTaskService = recordTaskService;
    }

    @GetMapping
    public List<RecordTask> getAll() {
        return recordTaskService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecordTask> getById(@PathVariable UUID id) {
        return recordTaskService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RecordTask> create(@Valid @RequestBody RecordTask recordTask) {
        RecordTask saved = recordTaskService.save(recordTask);
        return ResponseEntity.created(URI.create("/api/record-tasks/" + saved.getId())).body(saved);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<RecordTask>> createBulk(@Valid @RequestBody List<RecordTask> recordTasks) {
        if (recordTasks == null || recordTasks.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        List<RecordTask> saved = recordTaskService.saveAll(recordTasks);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecordTask> update(@PathVariable UUID id, @Valid @RequestBody RecordTask request) {
        return recordTaskService.findById(id)
                .map(existing -> {
                    existing.setRecord(request.getRecord());
                    existing.setItem(request.getItem());
                    existing.setItemName(request.getItemName());
                    existing.setItemLocation(request.getItemLocation());
                    existing.setStatus(request.getStatus());
                    return ResponseEntity.ok(recordTaskService.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!recordTaskService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        recordTaskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
