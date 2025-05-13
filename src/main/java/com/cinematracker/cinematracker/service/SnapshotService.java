package com.cinematracker.cinematracker.service;

import com.cinematracker.cinematracker.factoryPattern.SnapshotFactory;
import com.cinematracker.cinematracker.model.Snapshots;
import com.cinematracker.cinematracker.model.UpcomingMoviesSnapshot;
import com.cinematracker.cinematracker.repository.MovieSnapshotRepository;
import com.cinematracker.cinematracker.repository.SnapshotRepository;
import com.cinematracker.cinematracker.repository.UpcomingMoviesSnapshotRepository;
import com.cinematracker.cinematracker.repository.UpcomingSnapshotsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SnapshotService {

    @Autowired
    private SnapshotRepository snapshotRepository;
    @Autowired
    private UpcomingMoviesSnapshotRepository upcomingMoviesSnapshotRepository ;
    @Autowired
    private MovieSnapshotRepository movieSnapshotRepository ;
    @Autowired
    private UpcomingSnapshotsRepository upcomingSnapshotRepository;

    //Create snapshot vha. factory pattern
    public Snapshots createNewSnapshot(LocalDateTime createdAt) {
        if (createdAt.isBefore(LocalDateTime.now()) || createdAt == null) {
            throw new RuntimeException("CreateNewSnapshot error");
        }
        Snapshots snapshot = SnapshotFactory.createSnapshot(createdAt);
        return snapshotRepository.save(snapshot);

    }

    public boolean deleteSnapshot(Long snapshotId) {
        if (snapshotRepository.existsById(snapshotId)) {
            movieSnapshotRepository.deleteBySnapshotsId(snapshotId);
            snapshotRepository.deleteById(snapshotId);
            return true;
        }
        return false;
    }
    public boolean deleteUpcomingSnapshot(Long snapshotId) {
        if (upcomingSnapshotRepository.existsById(snapshotId)) {
            // Delete associated upcoming movie snapshots
            upcomingMoviesSnapshotRepository.deleteByUpcomingSnapshotId(snapshotId);
            // Delete the snapshot itself
            upcomingSnapshotRepository.deleteById(snapshotId);
            return true;
        }
        return false;
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
