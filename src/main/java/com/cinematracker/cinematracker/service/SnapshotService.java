package com.cinematracker.cinematracker.service;

import com.cinematracker.cinematracker.factoryPattern.SnapshotFactory;
import com.cinematracker.cinematracker.model.Snapshots;
import com.cinematracker.cinematracker.repository.SnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SnapshotService {

    @Autowired
    private SnapshotRepository snapshotRepository;

    //Create snapshot vha. factory pattern
    public Snapshots createNewSnapshot(LocalDateTime createdAt) {
        if (createdAt.isBefore(LocalDateTime.now()) || createdAt == null) {
            throw new RuntimeException("CreateNewSnapshot error");
        }
        Snapshots snapshot = SnapshotFactory.createSnapshot(createdAt);
        return snapshotRepository.save(snapshot);
    }







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
