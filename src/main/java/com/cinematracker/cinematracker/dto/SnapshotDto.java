package com.cinematracker.cinematracker.dto;

import java.time.LocalDateTime;

public class SnapshotDto {
    private Long id;
    private LocalDateTime createdAt;

    public SnapshotDto(Long id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
