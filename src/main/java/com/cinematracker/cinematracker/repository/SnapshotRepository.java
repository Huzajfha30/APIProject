package com.cinematracker.cinematracker.repository;

import com.cinematracker.cinematracker.model.Snapshots;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnapshotRepository extends JpaRepository<Snapshots, Long> {
    Snapshots findTopByOrderByCreatedAtDesc();

}
