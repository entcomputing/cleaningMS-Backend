package com.cleantracksystem.cleantrack.repository;

import com.cleantracksystem.cleantrack.model.Cleaner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CleanerRepository extends JpaRepository<Cleaner, UUID> {
}
