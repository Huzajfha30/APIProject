package com.cinematracker.cinematracker.controller;

import com.cinematracker.cinematracker.dto.tmdbResponse;

import com.cinematracker.cinematracker.service.TmdbService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173") // hvis du har React frontend
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final TmdbService tmdbService;

    public MovieController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/popular")
    public tmdbResponse getPopularMovies() {
        return tmdbService.getPopularMovies();
    }

    @GetMapping("/upcoming")
    public tmdbResponse getUpcomingMovies() {
        return tmdbService.getUpcomingMovies();
    }
}
