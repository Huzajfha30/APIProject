package com.cinematracker.cinematracker.controller;


import com.cinematracker.cinematracker.model.Movie;
import com.cinematracker.cinematracker.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")


@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired //dependency injection - spring automatisk instans af MovieRepository, så ingen: MovieRepository repo = new MovieRepository();
    private MovieRepository movieRepo;

    @GetMapping //metoden kaldes ved GET request - Fx henter data i react med fetch('/movies')
    public List<Movie> getAllMovies() {
        return movieRepo.findAll();
    }

    @PostMapping//metoden kaldes ved POST request til ('/movies'). fx indsende data fra React
    public Movie addMovie(@RequestBody Movie movie) {
        return movieRepo.save(movie);
    }
}
