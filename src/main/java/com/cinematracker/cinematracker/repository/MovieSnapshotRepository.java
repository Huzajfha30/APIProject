package com.cinematracker.cinematracker.repository;

import com.cinematracker.cinematracker.model.MovieSnapshots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MovieSnapshotRepository extends JpaRepository<MovieSnapshots, Long> {

    List<MovieSnapshots> findBySnapshotsId(Long snapshotsId);
    List<MovieSnapshots> findByMovieIdOrderBySnapshotsCreatedAtAsc(Long movieId);
    List<MovieSnapshots> findByMovieTitle(String title);

    @Transactional
    void deleteBySnapshotsId(Long snapshotId);

}