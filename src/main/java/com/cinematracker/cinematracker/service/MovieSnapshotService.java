package com.cinematracker.cinematracker.service;

import com.cinematracker.cinematracker.model.Movie_snapshots;
import com.cinematracker.cinematracker.repository.MovieSnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieSnapshotService {

    @Autowired
    private MovieSnapshotRepository movieSnapshotRepository;

    public List<Movie_snapshots> getAll() {
        return movieSnapshotRepository.findAll();
    }


    public Movie_snapshots save(Movie_snapshots ms) {
        return movieSnapshotRepository.save(ms);
    }
}
