package com.cinematracker.cinematracker.service;

import com.cinematracker.cinematracker.dto.TMDBMovieDto;
import com.cinematracker.cinematracker.dto.TMDBResponseDto;
import com.cinematracker.cinematracker.model.*;
import com.cinematracker.cinematracker.repository.MovieSnapshotRepository;
import com.cinematracker.cinematracker.repository.UpcomingMovieSnapshotsRepository;
import com.cinematracker.cinematracker.repository.UpcomingSnapshotsRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor

@Service
public class MovieSnapshotService {

    @Autowired
    private MovieSnapshotRepository movieSnapshotRepository;
    @Autowired
    private SnapshotService snapshotService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private TMDBService tmdbService;
    @Autowired
    private UpcomingMovieSnapshotsRepository upcomingMovieSnapshotsRepository;
    @Autowired
    UpcomingSnapshotsRepository upcomingSnapshotRepository;


    public List<MovieSnapshots> getAll() {
        return movieSnapshotRepository.findAll();
    }

    public MovieSnapshots save(MovieSnapshots ms) {
        return movieSnapshotRepository.save(ms);
    }

    private void saveMoviesToSnapshot(TMDBResponseDto tmdbResponseDto, Snapshots snapshot) {
        for (TMDBMovieDto movieDto : tmdbResponseDto.getResults()) {
            // Create or update Movie entity
            Movie movie = new Movie();
            movie.setTitle(movieDto.getTitle());
            movie.setReleaseDate(movieDto.getReleaseDate());
            movie.setPosterPath(movieDto.getPosterPath());
            movie = movieService.saveMovie(movie);

            // Create MovieSnapshots entity linking movie to snapshot
            MovieSnapshots movieSnapshot = new MovieSnapshots();
            movieSnapshot.setMovie(movie);
            movieSnapshot.setSnapshots(snapshot);
            movieSnapshot.setRating(movieDto.getRating());
            movieSnapshot.setVoteCount(movieDto.getVoteCount());


            save(movieSnapshot);
        }
    }

    public Snapshots fetchAndSaveMoviesAsSnapshot() {

        Snapshots snapshots = snapshotService.createNewSnapshot(LocalDateTime.now());
        TMDBResponseDto tmdbResponseDto = tmdbService.getNowPlayingMovies();
        saveMoviesToSnapshot(tmdbResponseDto, snapshots);

        return snapshots;
    }


    public List<UpcomingMovieSnapshots> fetchAndSaveUpcomingMovies() {
        TMDBResponseDto response = tmdbService.getUpcomingMovies();

        UpcomingSnapshot upcomingSnapshot = new UpcomingSnapshot();
        upcomingSnapshot.setCreatedAt(LocalDateTime.now());
        UpcomingSnapshot savedUpcomingSnapshot = upcomingSnapshotRepository.save(upcomingSnapshot);

        List<UpcomingMovieSnapshots> savedSnapshots = new ArrayList<>();

        for (TMDBMovieDto dto : response.getResults()) {
            // Create and save the Movie entity first
            Movie movie = new Movie();
            movie.setTitle(dto.getTitle());
            movie.setReleaseDate(dto.getReleaseDate());
            movie.setRating(dto.getRating());
            movie.setVoteCount(dto.getVoteCount());
            movie.setPosterPath(dto.getPosterPath());
            movie.setTmdbId(dto.getId());
            movie = movieService.saveMovie(movie);

            // Then create the UpcomingMovieSnapshots entity with the Movie
            UpcomingMovieSnapshots upcoming = new UpcomingMovieSnapshots();
            upcoming.setMovie(movie);
            upcoming.setUpcomingSnapshot(savedUpcomingSnapshot);
            upcoming.setRating(dto.getRating());
            upcoming.setVoteCount(dto.getVoteCount());

            savedSnapshots.add(upcomingMovieSnapshotsRepository.save(upcoming));
        }

        return savedSnapshots;
    }


}
