package com.cleantracksystem.cleantrack.repository;

import com.cleantracksystem.cleantrack.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IssueRepository extends JpaRepository<Issue, UUID> {
}
