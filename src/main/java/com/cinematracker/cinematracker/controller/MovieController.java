package com.cinematracker.cinematracker.controller;


import com.cinematracker.cinematracker.dto.TMDBResponseDto;
import com.cinematracker.cinematracker.model.Movie;
import com.cinematracker.cinematracker.model.MovieSnapshots;
import com.cinematracker.cinematracker.model.Snapshots;
import com.cinematracker.cinematracker.repository.MovieRepository;
import com.cinematracker.cinematracker.repository.MovieSnapshotRepository;
import com.cinematracker.cinematracker.repository.SnapshotRepository;
import com.cinematracker.cinematracker.service.MovieSnapshotService;
import com.cinematracker.cinematracker.service.SnapshotService;
import com.cinematracker.cinematracker.service.TMDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
//CRUD class


@CrossOrigin(origins = "http://localhost:5173") //tillader front end at kommunikere med backend


@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    //dependency injection - spring automatisk instans af MovieRepository, så ingen: MovieRepository repo = new MovieRepository();
    private MovieRepository movieRepo;
    @Autowired
    private SnapshotRepository snapshotRepo;
    @Autowired
    private MovieSnapshotRepository movieSnapshotRepo;
    @Autowired
    private MovieSnapshotService movieSnapshotService;
    @Autowired
    private SnapshotService snapshotService;
    @Autowired
    private TMDBService tmdbService;

    // Henter alle snapshots (bruges fx til dropdown)
    @GetMapping("/snapshots")
    public List<Snapshots> getAllSnapshots() {
        return snapshotRepo.findAll();
    }

    // Henter alle snapshots’ datoer (bruges til visning i frontend)
    @GetMapping("/snapshot-dates")
    public List<LocalDateTime> getSnapshotDates() {
        return snapshotRepo.findAll()
                .stream()
                .map(Snapshots::getCreatedAt)
                .collect(Collectors.toList());
    }

    // Henter nyeste snapshot med tilhørende film
    @GetMapping("/latest")
    public List<MovieSnapshots> getLatestSnapshot() {
        Snapshots latest = snapshotRepo.findTopByOrderByCreatedAtDesc();
        return movieSnapshotRepo.findBySnapshotsId(latest.getId());
    }

    @GetMapping("/snapshot/{snapshotId}")
    public ResponseEntity<List<MovieSnapshots>> getMovieSnapshotsBySnapshotId(@PathVariable String snapshotId) {
        if (snapshotId.equals("snapshots")) {
            // Håndter det specielle tilfælde
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Long snapshotIdLong = Long.valueOf(snapshotId); // Konverterer den til Long
        List<MovieSnapshots> movieSnapshots = movieSnapshotRepo.findBySnapshotsId(snapshotIdLong);
        if (movieSnapshots.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(movieSnapshots);
    }


    // Henter film der spiller nu (fra TMDB)
    @GetMapping("/now-playing")
    public ResponseEntity<TMDBResponseDto> getNowPlayingMovies() {
        return ResponseEntity.ok(tmdbService.getNowPlayingMovies());
    }

    // Henter og gemmer film som snapshot – kald fra knap i UI
    @PostMapping("/fetch-movies")
    public ResponseEntity<Snapshots> fetchAndSaveMovies() {
        Snapshots snapshots = movieSnapshotService.fetchAndSaveMoviesAsSnapshot();
        return ResponseEntity.status(HttpStatus.CREATED).body(snapshots);
    }

    // Tilføjer ny film manuelt
    @PostMapping
    public Movie addMovie(@RequestBody Movie movie) {
        return movieRepo.save(movie);
    }
}
