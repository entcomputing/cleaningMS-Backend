package com.cleantracksystem.cleantrack.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class AuditModel {

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
