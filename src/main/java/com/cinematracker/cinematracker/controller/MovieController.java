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
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
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

    @GetMapping("/snapshot")
    public List<Snapshots> getAllSnapshots() {
        return snapshotRepo.findAll();
    }

    @GetMapping("/snapshot-dates")
    public List<LocalDateTime> getSnapshotDates() {
        return snapshotRepo.findAll()
                .stream()
                .map(Snapshots::getCreatedAt)
                .collect(Collectors.toList());
    }

    @GetMapping("/latest")
    public ResponseEntity<List<MovieSnapshots>> getLatestSnapshot() {
        Snapshots latest = snapshotRepo.findTopByOrderByCreatedAtDesc();
        if (latest == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<MovieSnapshots> movieSnapshots = movieSnapshotRepo.findBySnapshotsId(latest.getId());
        return ResponseEntity.ok(movieSnapshots);
    }

    @GetMapping("/snapshot/{snapshotId}")
    public ResponseEntity<List<MovieSnapshots>> getMovieSnapshotsBySnapshotId(@PathVariable String snapshotId) {
        try {
            Long snapshotIdLong = Long.parseLong(snapshotId);
            Optional<Snapshots> snapshot = snapshotRepo.findById(snapshotIdLong);

            if (snapshot.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            List<MovieSnapshots> movieSnapshots = movieSnapshotRepo.findBySnapshotsId(snapshotIdLong);
            return ResponseEntity.ok(movieSnapshots);
        } catch (NumberFormatException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/now-playing")
    public ResponseEntity<TMDBResponseDto> getNowPlayingMovies() {
        return ResponseEntity.ok(tmdbService.getNowPlayingMovies());
    }

    @PostMapping("/fetch-movies")
    public ResponseEntity<List<MovieSnapshots>> fetchAndSaveMovies() {
        Snapshots snapshot = movieSnapshotService.fetchAndSaveMoviesAsSnapshot();
        List<MovieSnapshots> savedSnapshotMovies = movieSnapshotRepo.findBySnapshotsId(snapshot.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSnapshotMovies);
    }

    @PostMapping
    public Movie addMovie(@RequestBody Movie movie) {
        return movieRepo.save(movie);
    }
}
