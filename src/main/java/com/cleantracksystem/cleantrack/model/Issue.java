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
@Table(name = "issues")
@Getter
@Setter
@NoArgsConstructor
public class Issue {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private CleaningRecord record;

    @JsonProperty("item_name")
    @Column(name = "item_name", nullable = false)
    private String itemName = "";

    @JsonProperty("issue_type")
    @Column(name = "issue_type", nullable = false)
    private String issueType;

    @Column(name = "notes", nullable = false)
    private String notes = "";

    @JsonProperty("photo_url")
    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "status", nullable = false)
    private String status = "open";

    @JsonProperty("resolved_at")
    @Column(name = "resolved_at")
    private OffsetDateTime resolvedAt;

    @JsonProperty("resolved_by")
    @Column(name = "resolved_by")
    private UUID resolvedBy;

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
            this.status = "open";
        }
    }
}
