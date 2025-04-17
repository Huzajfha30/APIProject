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

    @GetMapping("/movies")//metoden kaldes ved GET request - Fx henter data i react med fetch('/movies')
    public List<Snapshots> getAllSnapshots() {
        return snapshotRepo.findAll();
    }

    @PostMapping("/snapshot") //endpoint til at oprette ny snapshot
    public ResponseEntity<Snapshots> createSnapshot(@RequestBody LocalDateTime createdAt) {
        Snapshots snapshot = snapshotService.createNewSnapshot(createdAt);
        return ResponseEntity.status(HttpStatus.CREATED).body(snapshot);
    }

    @GetMapping("/{snapshotId}") //hente specifik snapshot - dette er et requirement
    public ResponseEntity<List<MovieSnapshots>> getMovieSnapshotsById(@PathVariable Long snapshotId) {
        List<MovieSnapshots> movieSnapshots = movieSnapshotRepo.findBySnapshotsId(snapshotId);
        if (movieSnapshots.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); //404 error
        }
        return ResponseEntity.ok(movieSnapshots); // 200 ok
    }

    @GetMapping("/snapshot-dates") //endpoint der henter alle snapshot datoer, hænger sammen med øvrige
    public List<LocalDateTime> getSnapshotDates() {
        return snapshotRepo.findAll().stream().map(Snapshots::getCreatedAt).collect(Collectors.toList());
    }

    @GetMapping("/latest") //viser nyeste snapshot automatisk i drop down menu
    public List<MovieSnapshots> getLatestSnapshot() {
        Snapshots latest = snapshotRepo.findTopByOrderByCreatedAtDesc();
        return movieSnapshotRepo.findBySnapshotsId(latest.getId());
    }


    @PostMapping//metoden kaldes ved POST request til ('/movies'). fx indsende data fra React
    public Movie addMovie(@RequestBody Movie movie) {
        return movieRepo.save(movie);
    }

    @GetMapping("/now-playing")
    public ResponseEntity<TMDBResponseDto> getNowPlayingMovies() {
        return ResponseEntity.ok(tmdbService.getNowPlayingMovies());
    }

    @PostMapping("/fetch-movies") //manualt kald til at hente og gemme film
    public ResponseEntity<Snapshots> fetchAndSaveMovies() {
        Snapshots snapshots = movieSnapshotService.fetchAndSaveMoviesAsSnapshot();
        return ResponseEntity.status(HttpStatus.CREATED).body(snapshots);
    }

}
