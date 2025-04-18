package com.cinematracker.cinematracker.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MovieSnapshots {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Ikke længere @JsonIgnore, så det vises i JSON-responsen

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "snapshot_id")
    private Snapshots snapshots;

    private double rating;

    private int votes;
}

