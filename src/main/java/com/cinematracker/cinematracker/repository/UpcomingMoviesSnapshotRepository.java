package com.cinematracker.cinematracker.repository;

import com.cinematracker.cinematracker.model.UpcomingMoviesSnapshot;
import com.cinematracker.cinematracker.model.UpcomingSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UpcomingMoviesSnapshotRepository extends JpaRepository<UpcomingMoviesSnapshot, Long> {

    List<UpcomingMoviesSnapshot> findByUpcomingSnapshot(UpcomingSnapshot upcomingSnapshot);
    @Transactional
    void deleteByUpcomingSnapshotId(Long snapshotId);
}
