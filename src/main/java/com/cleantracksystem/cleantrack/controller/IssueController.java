package com.cleantracksystem.cleantrack.controller;

import com.cleantracksystem.cleantrack.model.Issue;
import com.cleantracksystem.cleantrack.service.IssueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping
    public List<Issue> getAll() {
        return issueService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Issue> getById(@PathVariable UUID id) {
        return issueService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Issue> create(@Valid @RequestBody Issue issue) {
        Issue saved = issueService.save(issue);
        return ResponseEntity.created(URI.create("/api/issues/" + saved.getId())).body(saved);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Issue>> createBulk(@Valid @RequestBody List<Issue> issues) {
        if (issues == null || issues.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        List<Issue> saved = issueService.saveAll(issues);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Issue> update(@PathVariable UUID id, @Valid @RequestBody Issue request) {
        return issueService.findById(id)
                .map(existing -> {
                    existing.setRecord(request.getRecord());
                    existing.setItemName(request.getItemName());
                    existing.setIssueType(request.getIssueType());
                    existing.setNotes(request.getNotes());
                    existing.setPhotoUrl(request.getPhotoUrl());
                    existing.setStatus(request.getStatus());
                    existing.setResolvedAt(request.getResolvedAt());
                    existing.setResolvedBy(request.getResolvedBy());
                    return ResponseEntity.ok(issueService.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!issueService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        issueService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
