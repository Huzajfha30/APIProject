package com.cinematracker.cinematracker.service;

import com.cinematracker.cinematracker.model.Snapshots;
import com.cinematracker.cinematracker.repository.SnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SnapshotService {

    @Autowired
    private SnapshotRepository snapshotRepository;

    public List<Snapshots> getAllSnapshots() {
        return snapshotRepository.findAll();
    }

    public Snapshots getSnapshotById(Long id) {
        return snapshotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Snapshot not found ! custom error"));
    }

    public Snapshots saveSnapshot(Snapshots snapshot) {
        return snapshotRepository.save(snapshot);
    }
}
