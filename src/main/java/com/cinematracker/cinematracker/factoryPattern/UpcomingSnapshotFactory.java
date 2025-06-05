package com.cinematracker.cinematracker.factoryPattern;

import com.cinematracker.cinematracker.model.UpcomingSnapshot;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class UpcomingSnapshotFactory {

    // Singleton instans
    private static UpcomingSnapshotFactory instance;

    // Privat constructor (ingen kan lave new)
    private UpcomingSnapshotFactory() {}

    // Offentlig adgang til singleton-instansen
    public static UpcomingSnapshotFactory getInstance() {
        if (instance == null) {
            synchronized (UpcomingSnapshotFactory.class) {
                if (instance == null) {
                    instance = new UpcomingSnapshotFactory();
                }
            }
        }
        return instance;
    }

    // Factory-metode til at oprette upcoming snapshot
    public UpcomingSnapshot createSnapshot(LocalDateTime createdAt) {
        UpcomingSnapshot snapshot = new UpcomingSnapshot();
        snapshot.setCreatedAt(createdAt != null ? createdAt : LocalDateTime.now());
        snapshot.setMovieSnapshots(new ArrayList<>()); // altid tom liste til start!
        return snapshot;
    }

    // Factory-metode til default snapshot (nu)
    public UpcomingSnapshot createDefaultSnapshot() {
        return createSnapshot(LocalDateTime.now());
    }
}
