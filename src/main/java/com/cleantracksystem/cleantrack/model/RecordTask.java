package com.cleantracksystem.cleantrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "record_tasks")
@Getter
@Setter
@NoArgsConstructor
public class RecordTask {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    // Client sends { "record": { "id": ... } } to link a task to its parent
    // record, but must never receive the full (lazily-loaded) record back -
    // WRITE_ONLY keeps the field settable without serializing it on read.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private CleaningRecord record;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private CleaningItem item;

    @JsonProperty("item_name")
    @Column(name = "item_name", nullable = false)
    private String itemName;

    @JsonProperty("item_location")
    @Column(name = "item_location", nullable = false)
    private String itemLocation = "";

    @Column(name = "status", nullable = false)
    private String status = "not_started";

    @JsonProperty("created_at")
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @JsonProperty("record_id")
    public UUID getRecordId() {
        return record != null ? record.getId() : null;
    }

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        if (this.createdAt == null) {
            this.createdAt = OffsetDateTime.now();
        }
        if (this.status == null || this.status.isBlank()) {
            this.status = "not_started";
        }
    }
}
