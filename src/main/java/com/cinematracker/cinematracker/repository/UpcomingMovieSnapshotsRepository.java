package com.cinematracker.cinematracker.repository;

import com.cinematracker.cinematracker.model.UpcomingMovieSnapshots;
import com.cinematracker.cinematracker.model.UpcomingSnapshot;
import com.cinematracker.cinematracker.model.Movie; // Husk at importere Movie!
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UpcomingMovieSnapshotsRepository extends JpaRepository<UpcomingMovieSnapshots, Long> {

    List<UpcomingMovieSnapshots> findByUpcomingSnapshot(UpcomingSnapshot upcomingSnapshot);

    // Tilføj denne linje:
    List<UpcomingMovieSnapshots> findByMovie(Movie movie);

    List<UpcomingMovieSnapshots> findByMovie_Title(String title);

    @Transactional
    void deleteByUpcomingSnapshotId(Long snapshotId);
}
