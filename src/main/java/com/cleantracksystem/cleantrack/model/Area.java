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

// Areas are reusable "area types" (e.g. "Washroom"), not tied to one floor -
// the same Area (and its CleaningItems) can be attached to several floors
// instead of being recreated per floor. See CleaningItem's Shift relation
// for the identical ignore/transient/resolve pattern this follows.
@Entity
@Table(name = "areas")
@Getter
@Setter
@NoArgsConstructor
public class Area {

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

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "floor_areas",
            joinColumns = @JoinColumn(name = "area_id"),
            inverseJoinColumns = @JoinColumn(name = "floor_id"))
    private Set<Floor> floors = new HashSet<>();

    @Transient
    @JsonProperty("floor_ids")
    private List<UUID> floorIds = new ArrayList<>();

    // Which shifts this area's items show up under. Areas default to every
    // shift and are excluded one at a time as needed, rather than requiring
    // each shift to be opted into individually.
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "area_shifts",
            joinColumns = @JoinColumn(name = "area_id"),
            inverseJoinColumns = @JoinColumn(name = "shift_id"))
    private Set<Shift> shifts = new HashSet<>();

    @Transient
    @JsonProperty("shift_ids")
    private List<UUID> shiftIds = new ArrayList<>();

    @PostLoad
    public void postLoad() {
        this.floorIds = floors.stream().map(Floor::getId).collect(Collectors.toList());
        this.shiftIds = shifts.stream().map(Shift::getId).collect(Collectors.toList());
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
