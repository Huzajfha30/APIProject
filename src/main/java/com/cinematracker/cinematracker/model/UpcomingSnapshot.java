package com.cinematracker.cinematracker.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class UpcomingSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;


    @OneToMany(mappedBy = "upcomingSnapshot", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<UpcomingMovieSnapshots> movieSnapshots;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<UpcomingMovieSnapshots> getMovieSnapshots() {
        return movieSnapshots;
    }

    public void setMovieSnapshots(List<UpcomingMovieSnapshots> movieSnapshots) {
        this.movieSnapshots = movieSnapshots;
    }
}
