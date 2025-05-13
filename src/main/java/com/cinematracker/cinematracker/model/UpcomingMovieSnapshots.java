package com.cinematracker.cinematracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UpcomingMovieSnapshots {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private double rating;
    @Column(nullable = true)
    private Integer voteCount;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "upcoming_snapshot_id")
    private UpcomingSnapshot upcomingSnapshot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public UpcomingSnapshot getUpcomingSnapshot() {
        return upcomingSnapshot;
    }

    public void setUpcomingSnapshot(UpcomingSnapshot upcomingSnapshot) {
        this.upcomingSnapshot = upcomingSnapshot;
    }
}
