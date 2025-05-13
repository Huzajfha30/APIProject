package com.cinematracker.cinematracker.factoryPattern;

import com.cinematracker.cinematracker.model.Snapshots;

import java.time.LocalDateTime;

public class SnapshotFactory {

    // Singleton instance
    private static SnapshotFactory instance;

    // Private constructor to prevent external instantiation
    private SnapshotFactory() {
    }

    // Public static method to access the singleton instance
    public static SnapshotFactory getInstance() {
        if (instance == null) {
            synchronized (SnapshotFactory.class) {
                if (instance == null) {
                    instance = new SnapshotFactory();
                }
            }
        }
        return instance;
    }

    // Factory method to create a new snapshot
    public Snapshots createSnapshot(LocalDateTime createdAt) {
        Snapshots snapshots = new Snapshots();
        snapshots.setCreatedAt(createdAt);
        return snapshots;
    }
    }


   /* public static Snapshots createSnapshot(LocalDateTime createdAt) {
        Snapshots snapshots = new Snapshots();
        snapshots.setCreatedAt(createdAt);
        return snapshots;
    }*/ // før singleton patern

}
