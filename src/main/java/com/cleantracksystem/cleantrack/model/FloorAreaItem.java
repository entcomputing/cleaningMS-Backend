package com.cleantracksystem.cleantrack.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "floor_area_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"floor_id", "area_id", "item_id"}))
@Getter
@Setter
@NoArgsConstructor
public class FloorAreaItem {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @JsonProperty("floor_id")
    @Column(name = "floor_id", nullable = false)
    private UUID floorId;

    @JsonProperty("area_id")
    @Column(name = "area_id", nullable = false)
    private UUID areaId;

    @JsonProperty("item_id")
    @Column(name = "item_id", nullable = false)
    private UUID itemId;

    @JsonProperty("sort_order")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

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
    }
}
