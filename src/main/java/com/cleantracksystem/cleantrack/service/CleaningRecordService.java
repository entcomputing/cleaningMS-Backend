package com.cleantracksystem.cleantrack.service;

import com.cleantracksystem.cleantrack.model.CleaningRecord;
import com.cleantracksystem.cleantrack.repository.CleaningRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CleaningRecordService {

    private final CleaningRecordRepository cleaningRecordRepository;

    public CleaningRecordService(CleaningRecordRepository cleaningRecordRepository) {
        this.cleaningRecordRepository = cleaningRecordRepository;
    }

    @Transactional(readOnly = true)
    public List<CleaningRecord> findAll() {
        return cleaningRecordRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<CleaningRecord> findById(UUID id) {
        return cleaningRecordRepository.findById(id);
    }

    public CleaningRecord save(CleaningRecord cleaningRecord) {
        return cleaningRecordRepository.save(cleaningRecord);
    }

    public void deleteById(UUID id) {
        cleaningRecordRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return cleaningRecordRepository.existsById(id);
    }
}
