package com.cleantracksystem.cleantrack.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "admins")
@Getter
@Setter
@NoArgsConstructor
public class Admin {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @JsonProperty("full_name")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // Never serialized out, in either direction that matters: @JsonIgnore
    // blocks it from every response body; it's only ever set server-side by
    // AuthService after hashing a raw password.
    @JsonIgnore
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

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
