package com.cinematracker.cinematracker.repository;

import com.cinematracker.cinematracker.model.UpcomingSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpcomingSnapshotsRepository extends JpaRepository<UpcomingSnapshot,Long> {

}
