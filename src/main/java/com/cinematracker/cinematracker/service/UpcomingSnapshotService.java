package com.cinematracker.cinematracker.service;

import com.cinematracker.cinematracker.dto.TMDBMovieDto;
import com.cinematracker.cinematracker.dto.TMDBResponseDto;
import com.cinematracker.cinematracker.factoryPattern.UpcomingSnapshotFactory;
import com.cinematracker.cinematracker.model.*;
import com.cinematracker.cinematracker.repository.MovieRepository;
import com.cinematracker.cinematracker.repository.UpcomingMovieSnapshotsRepository;
import com.cinematracker.cinematracker.repository.UpcomingSnapshotsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UpcomingSnapshotService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UpcomingSnapshotsRepository upcomingSnapshotRepository;
    @Autowired
    private UpcomingMovieSnapshotsRepository upcomingMovieSnapshotsRepository;
    @Autowired
    private TMDBService tmdbService;

    public List<UpcomingMovieSnapshots> fetchAndSaveUpcomingMovies() {
        TMDBResponseDto response = tmdbService.getUpcomingMovies();


        UpcomingSnapshotFactory factory = UpcomingSnapshotFactory.getInstance();
        UpcomingSnapshot upcomingSnapshot = factory.createDefaultSnapshot();
        UpcomingSnapshot savedUpcomingSnapshot = upcomingSnapshotRepository.save(upcomingSnapshot);

        List<UpcomingMovieSnapshots> savedSnapshots = new ArrayList<>();

        for (TMDBMovieDto dto : response.getResults()) {

            Movie movie = new Movie();
            movie.setTitle(dto.getTitle());
            movie.setReleaseDate(dto.getReleaseDate());
            movie.setRating(dto.getRating());
            movie.setVoteCount(dto.getVoteCount());
            movie.setPosterPath(dto.getPosterPath());
            movie.setTmdbId(dto.getId());
            movie = movieRepository.save(movie);

            // Opret UpcomingMovieSnapshots
            UpcomingMovieSnapshots upcoming = new UpcomingMovieSnapshots();
            upcoming.setMovie(movie);
            upcoming.setUpcomingSnapshot(savedUpcomingSnapshot);
            upcoming.setRating(dto.getRating());
            upcoming.setVoteCount(dto.getVoteCount());

            savedSnapshots.add(upcomingMovieSnapshotsRepository.save(upcoming));
        }

        return savedSnapshots;
    }

    @Transactional
    public void addTestVotesForMovie(Long movieId, List<Long> snapshotIds, List<Integer> voteCounts, double rating) {
        if (snapshotIds.size() != voteCounts.size()) {
            throw new IllegalArgumentException("snapshotIds og voteCounts skal være lige lange!");
        }
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Film ikke fundet"));

        for (int i = 0; i < snapshotIds.size(); i++) {
            Long snapId = snapshotIds.get(i);
            Integer votes = voteCounts.get(i);

            UpcomingSnapshot snapshot = upcomingSnapshotRepository.findById(snapId)
                    .orElseThrow(() -> new RuntimeException("Snapshot ikke fundet"));


            boolean exists = upcomingMovieSnapshotsRepository
                    .findByUpcomingSnapshot(snapshot)
                    .stream()
                    .anyMatch(s -> s.getMovie().getId().equals(movieId));

            if (!exists) {
                UpcomingMovieSnapshots ums = new UpcomingMovieSnapshots();
                ums.setMovie(movie);
                ums.setUpcomingSnapshot(snapshot);
                ums.setVoteCount(votes);
                ums.setRating(rating);
                upcomingMovieSnapshotsRepository.save(ums);
            }
        }


    }
}
