package com.cleantracksystem.cleantrack.service;

import com.cleantracksystem.cleantrack.model.CleaningRecord;
import com.cleantracksystem.cleantrack.model.RecordTask;
import com.cleantracksystem.cleantrack.repository.CleaningRecordRepository;
import com.cleantracksystem.cleantrack.repository.RecordTaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class RecordTaskService {

    private final RecordTaskRepository recordTaskRepository;
    private final CleaningRecordRepository cleaningRecordRepository;

    public RecordTaskService(
            RecordTaskRepository recordTaskRepository, CleaningRecordRepository cleaningRecordRepository) {
        this.recordTaskRepository = recordTaskRepository;
        this.cleaningRecordRepository = cleaningRecordRepository;
    }

    @Transactional(readOnly = true)
    public List<RecordTask> findAll() {
        return recordTaskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<RecordTask> findById(UUID id) {
        return recordTaskRepository.findById(id);
    }

    public RecordTask save(RecordTask recordTask) {
        // The client only ever sends the parent record's id. A plain
        // deserialized CleaningRecord with just that id set is transient as
        // far as Hibernate is concerned and fails the FK check on save;
        // getReferenceById gives back a proxy Hibernate recognizes as
        // existing, without a round trip to load it.
        CleaningRecord record = recordTask.getRecord();
        if (record != null && record.getId() != null) {
            recordTask.setRecord(cleaningRecordRepository.getReferenceById(record.getId()));
        }
        return recordTaskRepository.save(recordTask);
    }

    public List<RecordTask> saveAll(List<RecordTask> recordTasks) {
        for (RecordTask recordTask : recordTasks) {
            CleaningRecord record = recordTask.getRecord();
            if (record != null && record.getId() != null) {
                recordTask.setRecord(cleaningRecordRepository.getReferenceById(record.getId()));
            }
        }
        return recordTaskRepository.saveAll(recordTasks);
    }

    public void deleteById(UUID id) {
        recordTaskRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return recordTaskRepository.existsById(id);
    }
}
