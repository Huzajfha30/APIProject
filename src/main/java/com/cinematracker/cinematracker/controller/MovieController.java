package com.cinematracker.cinematracker.controller;

import com.cinematracker.cinematracker.dto.SnapshotDto;
import com.cinematracker.cinematracker.dto.TMDBResponseDto;
import com.cinematracker.cinematracker.model.Movie;
import com.cinematracker.cinematracker.model.UpcomingMovieSnapshots;
import com.cinematracker.cinematracker.model.UpcomingSnapshot;
import com.cinematracker.cinematracker.repository.MovieRepository;
import com.cinematracker.cinematracker.repository.UpcomingMovieSnapshotsRepository;
import com.cinematracker.cinematracker.repository.UpcomingSnapshotsRepository;
import com.cinematracker.cinematracker.service.TMDBService;
import com.cinematracker.cinematracker.service.UpcomingSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

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
    private TMDBService tmdbService;
    @Autowired
    private UpcomingMovieSnapshotsRepository upcomingMovieSnapshotsRepository;
    @Autowired
    private UpcomingSnapshotsRepository upcomingSnapshotRepository;
    @Autowired
    private UpcomingSnapshotService upcomingSnapshotService;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger log = LoggerFactory.getLogger(MovieController.class);
    private final String tmdbApiKey = "a8bcc0a4ff086551c9ac55c980851b77";
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

    // --- UPCOMING MOVIES ---

    // (Hvis du kun vil vise fra DB, kan du lave et custom endpoint – men nedenfor viser fra TMDB API direkte)
    @GetMapping("/upcoming")
    public ResponseEntity<TMDBResponseDto> getUpcomingMovies() {
        return ResponseEntity.ok(tmdbService.getUpcomingMovies());
    }

    @PostMapping("/fetch-upcoming")
    public ResponseEntity<List<UpcomingMovieSnapshots>> fetchAndSaveUpcomingMovies() {
        List<UpcomingMovieSnapshots> snapshots = upcomingSnapshotService.fetchAndSaveUpcomingMovies();
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

    @PostMapping("/test-vote-history")
    public ResponseEntity<String> addTestVoteHistory(
            @RequestParam Long movieId,
            @RequestParam List<Long> snapshotIds,
            @RequestParam List<Integer> voteCounts,
            @RequestParam double rating
    ) {
        try {
            upcomingSnapshotService.addTestVotesForMovie(movieId, snapshotIds, voteCounts, rating);
            return ResponseEntity.ok("Test votes added!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Fejl: " + e.getMessage());
        }
    }
    @GetMapping("/upcoming-vote-history/{movieId}")
    public List<UpcomingMovieSnapshots> getVoteHistory(@PathVariable Long movieId) {
        Movie movie = movieRepo.findById(movieId).orElseThrow();
        return upcomingMovieSnapshotsRepository.findByMovie(movie);
    }
    @GetMapping("/now-playing-single")
    public ResponseEntity<?> getNowPlayingSingle() {
        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + tmdbApiKey + "&language=en-US&page=1";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return ResponseEntity.ok(response.getBody());
    }


}
