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



}
