package com.cinematracker.cinematracker.factoryPattern;

import com.cinematracker.cinematracker.model.Snapshots;

import java.time.LocalDateTime;

public class SnapshotFactory {

    public static Snapshots createSnapshot(LocalDateTime createdAt) {
        Snapshots snapshots = new Snapshots();
        snapshots.setCreated_at(createdAt);
        return snapshots;
    }

}
