package com.cinematracker.cinematracker.repository;

import com.cinematracker.cinematracker.model.UpcomingMovieSnapshots;
import com.cinematracker.cinematracker.model.UpcomingSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UpcomingMovieSnapshotsRepository extends JpaRepository<UpcomingMovieSnapshots, Long> {

    List<UpcomingMovieSnapshots> findByUpcomingSnapshot(UpcomingSnapshot upcomingSnapshot);
    @Transactional
    void deleteByUpcomingSnapshotId(Long snapshotId);
}
