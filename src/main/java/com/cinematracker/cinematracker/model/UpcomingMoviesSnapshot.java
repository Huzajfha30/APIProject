package com.cinematracker.cinematracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class UpcomingMoviesSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tmdbId;
    private String title;
    private Date releaseDate;
    private double rating;
    private int voteCount;
    private String posterPath;

    private Date createdAt; // eller LocalDateTime, alt efter hvad du bruger

    @ManyToOne
    @JoinColumn(name = "upcoming_snapshot_id")
    private UpcomingSnapshot upcomingSnapshot;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(Long tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UpcomingSnapshot getUpcomingSnapshot() {
        return upcomingSnapshot;
    }

    public void setUpcomingSnapshot(UpcomingSnapshot upcomingSnapshot) {
        this.upcomingSnapshot = upcomingSnapshot;
    }
}
