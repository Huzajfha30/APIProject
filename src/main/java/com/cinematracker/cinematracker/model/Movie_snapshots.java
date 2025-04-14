package com.cinematracker.cinematracker.model;


import jakarta.persistence.*;

@Entity
public class Movie_snapshots {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "snapshot_id")
    private Snapshots snapshots;

    private double rating;
    private int votes;
}
