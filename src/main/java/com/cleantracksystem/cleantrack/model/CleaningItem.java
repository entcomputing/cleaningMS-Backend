package com.cleantracksystem.cleantrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

// Items are reusable too (e.g. "Windows" cuts across Washroom, Office, ...) -
// attached to whichever Areas actually have them, same shape as Area<->Floor.
@Entity
@Table(name = "cleaning_items")
@Getter
@Setter
@NoArgsConstructor
public class CleaningItem {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @JsonProperty("sort_order")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @JsonProperty("is_active")
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @JsonProperty("created_at")
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    // Ignored on serialization (lazy collection of full entities isn't safe
    // to serialize without jackson-datatype-hibernate) - areaIds below is the
    // actual read/write JSON contract, kept in sync via @PostLoad and
    // resolved into real references by CleaningItemService.
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "area_items",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "area_id"))
    private Set<Area> areas = new HashSet<>();

    @Transient
    @JsonProperty("area_ids")
    private List<UUID> areaIds = new ArrayList<>();

    @PostLoad
    public void postLoad() {
        this.areaIds = areas.stream().map(Area::getId).collect(Collectors.toList());
    }

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
