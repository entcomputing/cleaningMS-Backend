package com.cleantracksystem.cleantrack.repository;

import com.cleantracksystem.cleantrack.model.CleaningItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface CleaningItemRepository extends JpaRepository<CleaningItem, UUID> {
    List<CleaningItem> findAllByIdIn(Collection<UUID> ids);
}
