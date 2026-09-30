package com.cleantracksystem.cleantrack.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "shifts")
@Getter
@Setter
@NoArgsConstructor
public class Shift {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @JsonProperty("start_time")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @JsonProperty("end_time")
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    // JSON, not SqlTypes.ARRAY: MySQL has no native array column type (this
    // needs to work on both H2 for local dev and MySQL for the remote db),
    // and JSON is the one array-ish representation all three dialects support.
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "days", nullable = false)
    private Short[] days = new Short[] {1, 2, 3, 4, 5};

    @JsonProperty("is_active")
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @JsonProperty("created_at")
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        if (this.createdAt == null) {
            this.createdAt = OffsetDateTime.now();
        }
        if (this.days == null || this.days.length == 0) {
            this.days = new Short[] {1, 2, 3, 4, 5};
        }
    }
}
