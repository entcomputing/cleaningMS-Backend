package com.cleantracksystem.cleantrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cleaning_records")
@Getter
@Setter
@NoArgsConstructor
public class CleaningRecord {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "reference", nullable = false, unique = true)
    private String reference;

    // Ignored on both directions: a client only ever sends/reads the id via
    // cleaner_name below, and serializing a lazy Hibernate proxy here isn't safe
    // without the jackson-datatype-hibernate module (not on the classpath).
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cleaner_id")
    private Cleaner cleaner;

    @JsonProperty("cleaner_name")
    @Column(name = "cleaner_name", nullable = false)
    private String cleanerName;

    @JsonProperty("supervisor_name")
    @Column(name = "supervisor_name", nullable = false)
    private String supervisorName = "";

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @JsonProperty("shift_name")
    @Column(name = "shift_name", nullable = false)
    private String shiftName = "";

    @JsonProperty("record_date")
    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @JsonProperty("tasks_total")
    @Column(name = "tasks_total", nullable = false)
    private Integer tasksTotal = 0;

    @JsonProperty("tasks_completed")
    @Column(name = "tasks_completed", nullable = false)
    private Integer tasksCompleted = 0;

    @JsonProperty("issues_count")
    @Column(name = "issues_count", nullable = false)
    private Integer issuesCount = 0;

    @JsonProperty("inspection_status")
    @Column(name = "inspection_status", nullable = false)
    private String inspectionStatus = "pending";

    @JsonProperty("inspection_notes")
    @Column(name = "inspection_notes", nullable = false)
    private String inspectionNotes = "";

    @JsonProperty("inspected_at")
    @Column(name = "inspected_at")
    private OffsetDateTime inspectedAt;

    @JsonProperty("submitted_at")
    @Column(name = "submitted_at", nullable = false)
    private OffsetDateTime submittedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecordTask> recordTasks = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Issue> issues = new ArrayList<>();

    @JsonProperty("cleaner_id")
    public UUID getCleanerId() {
        return cleaner != null ? cleaner.getId() : null;
    }

    @JsonProperty("shift_id")
    public UUID getShiftId() {
        return shift != null ? shift.getId() : null;
    }

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        if (this.reference == null || this.reference.isBlank()) {
            this.reference = generateReference();
        }
        if (this.recordDate == null) {
            this.recordDate = LocalDate.now();
        }
        if (this.submittedAt == null) {
            this.submittedAt = OffsetDateTime.now();
        }
        if (this.tasksTotal == null) {
            this.tasksTotal = 0;
        }
        if (this.tasksCompleted == null) {
            this.tasksCompleted = 0;
        }
        if (this.issuesCount == null) {
            this.issuesCount = 0;
        }
        if (this.inspectionStatus == null || this.inspectionStatus.isBlank()) {
            this.inspectionStatus = "pending";
        }
    }

    private String generateReference() {
        int year = OffsetDateTime.now().getYear();
        int suffix = (int) (Math.random() * 10000);
        return String.format("CR-%d-%04d", year, suffix);
    }
}
