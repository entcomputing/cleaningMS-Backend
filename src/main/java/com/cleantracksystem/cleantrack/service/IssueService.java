package com.cleantracksystem.cleantrack.service;

import com.cleantracksystem.cleantrack.model.CleaningRecord;
import com.cleantracksystem.cleantrack.model.Issue;
import com.cleantracksystem.cleantrack.repository.CleaningRecordRepository;
import com.cleantracksystem.cleantrack.repository.IssueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class IssueService {

    private final IssueRepository issueRepository;
    private final CleaningRecordRepository cleaningRecordRepository;

    public IssueService(IssueRepository issueRepository, CleaningRecordRepository cleaningRecordRepository) {
        this.issueRepository = issueRepository;
        this.cleaningRecordRepository = cleaningRecordRepository;
    }

    @Transactional(readOnly = true)
    public List<Issue> findAll() {
        return issueRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Issue> findById(UUID id) {
        return issueRepository.findById(id);
    }

    public Issue save(Issue issue) {
        // See RecordTaskService.save: the client only sends the parent
        // record's id, which needs resolving to a real JPA reference.
        CleaningRecord record = issue.getRecord();
        if (record != null && record.getId() != null) {
            issue.setRecord(cleaningRecordRepository.getReferenceById(record.getId()));
        }
        return issueRepository.save(issue);
    }

    public List<Issue> saveAll(List<Issue> issues) {
        for (Issue issue : issues) {
            CleaningRecord record = issue.getRecord();
            if (record != null && record.getId() != null) {
                issue.setRecord(cleaningRecordRepository.getReferenceById(record.getId()));
            }
        }
        return issueRepository.saveAll(issues);
    }

    public void deleteById(UUID id) {
        issueRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return issueRepository.existsById(id);
    }
}
