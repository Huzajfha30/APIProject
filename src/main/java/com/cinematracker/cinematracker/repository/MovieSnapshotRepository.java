package com.cinematracker.cinematracker.repository;

import com.cinematracker.cinematracker.model.Movie_snapshots;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieSnapshotRepository extends JpaRepository<Movie_snapshots, Long> {

}
