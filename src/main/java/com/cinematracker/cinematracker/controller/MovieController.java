package com.cinematracker.cinematracker.controller;


import com.cinematracker.cinematracker.dto.SnapshotDto;
import com.cinematracker.cinematracker.dto.TMDBResponseDto;
import com.cinematracker.cinematracker.model.*;
import com.cinematracker.cinematracker.repository.*;
import com.cinematracker.cinematracker.service.MovieSnapshotService;
import com.cinematracker.cinematracker.service.SnapshotService;
import com.cinematracker.cinematracker.service.TMDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    @Autowired
    private UpcomingMovieSnapshotsRepository upcomingMovieSnapshotsRepository;
    @Autowired
    private UpcomingSnapshotsRepository upcomingSnapshotRepository;

    private static final Logger log = LoggerFactory.getLogger(MovieController.class);

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
    @DeleteMapping("/snapshot/{snapshotId}")
    public ResponseEntity<String> deleteSnapshot(@PathVariable Long snapshotId) {
        boolean deleted = snapshotService.deleteSnapshot(snapshotId);
        if (deleted) {
            return ResponseEntity.ok("Snapshot deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Snapshot not found");
        }
    }

    @DeleteMapping("/upcoming-snapshot/{id}")
    public ResponseEntity<?> deleteUpcomingSnapshot(@PathVariable Long id) {
        try {
            if (!upcomingSnapshotRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            upcomingSnapshotRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting snapshot: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not delete snapshot: " + e.getMessage());
        }
    }


    @PostMapping
    public Movie addMovie(@RequestBody Movie movie) {
        return movieRepo.save(movie);
    }


    // UPCOMING MOVIES Handling - NEW Structure
    @GetMapping("/upcoming")
    public ResponseEntity<TMDBResponseDto> getUpcomingMovies() {
        return ResponseEntity.ok(tmdbService.getUpcomingMovies());
    }

    @PostMapping("/fetch-upcoming")
    public ResponseEntity<List<UpcomingMovieSnapshots>> fetchAndSaveUpcomingMovies() {
        List<UpcomingMovieSnapshots> snapshots = movieSnapshotService.fetchAndSaveUpcomingMovies();
        return ResponseEntity.status(HttpStatus.CREATED).body(snapshots);
    }

    @GetMapping("/upcoming-snapshot-dates")
    public List<SnapshotDto> getUpcomingSnapshotDates() {
        return upcomingSnapshotRepository.findAll().stream()
                .map(s -> new SnapshotDto(s.getId(), s.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @GetMapping("/upcoming-snapshots/{snapshotId}")
    public List<UpcomingMovieSnapshots> getUpcomingMoviesBySnapshotId(@PathVariable Long snapshotId) {
        UpcomingSnapshot snapshot = upcomingSnapshotRepository.findById(snapshotId).orElseThrow();
        return upcomingMovieSnapshotsRepository.findByUpcomingSnapshot(snapshot);
    }
    @GetMapping("/movie-vote-history/by-title/{title}")
    public List<MovieSnapshots> getMovieHistoryByTitle(@PathVariable String title) {
        return movieSnapshotRepo.findByMovieTitle(title);
    }

}
