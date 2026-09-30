package com.cleantracksystem.cleantrack.repository;

import com.cleantracksystem.cleantrack.model.FloorAreaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FloorAreaItemRepository extends JpaRepository<FloorAreaItem, UUID> {
    List<FloorAreaItem> findByFloorIdAndAreaId(UUID floorId, UUID areaId);
}
