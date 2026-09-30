package com.cleantracksystem.cleantrack.repository;

import com.cleantracksystem.cleantrack.model.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FloorRepository extends JpaRepository<Floor, UUID> {
}
