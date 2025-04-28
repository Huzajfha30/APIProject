package com.cinematracker.cinematracker.service;

import com.cinematracker.cinematracker.dto.TMDBMovieDto;
import com.cinematracker.cinematracker.dto.TMDBResponseDto;
import com.cinematracker.cinematracker.model.*;
import com.cinematracker.cinematracker.repository.MovieSnapshotRepository;
import com.cinematracker.cinematracker.repository.SnapshotRepository;
import com.cinematracker.cinematracker.repository.UpcomingMoviesSnapshotRepository;
import com.cinematracker.cinematracker.repository.UpcomingSnapshotsRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

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
    private UpcomingMoviesSnapshotRepository upcomingMoviesSnapshotRepository;
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
            movie = movieService.saveMovie(movie);

            // Create MovieSnapshots entity linking movie to snapshot
            MovieSnapshots movieSnapshot = new MovieSnapshots();
            movieSnapshot.setMovie(movie);
            movieSnapshot.setSnapshots(snapshot);
            movieSnapshot.setRating(movieDto.getRating());
            movieSnapshot.setVotes(movieDto.getVoteCount());

            save(movieSnapshot);
        }
    }

    public Snapshots fetchAndSaveMoviesAsSnapshot() {

        Snapshots snapshots = snapshotService.createNewSnapshot(LocalDateTime.now());
        TMDBResponseDto tmdbResponseDto = tmdbService.getNowPlayingMovies();
        saveMoviesToSnapshot(tmdbResponseDto, snapshots);

        return snapshots;
    }


    public UpcomingSnapshot fetchAndSaveUpcomingMovies() {
        TMDBResponseDto response = tmdbService.getUpcomingMovies();

        UpcomingSnapshot upcomingSnapshot = new UpcomingSnapshot();
        upcomingSnapshot.setCreatedAt(LocalDateTime.now());
        UpcomingSnapshot savedUpcomingSnapshot = upcomingSnapshotRepository.save(upcomingSnapshot);

        for (TMDBMovieDto dto : response.getResults()) {
            UpcomingMoviesSnapshot upcoming = new UpcomingMoviesSnapshot();
            upcoming.setTmdbId(dto.getId());
            upcoming.setTitle(dto.getTitle());
            upcoming.setReleaseDate(dto.getReleaseDate());
            upcoming.setRating(dto.getRating());
            upcoming.setVoteCount(dto.getVoteCount());
            upcoming.setPosterPath(dto.getPosterPath());
            upcoming.setCreatedAt(java.sql.Date.valueOf(LocalDate.now()));
            upcoming.setUpcomingSnapshot(savedUpcomingSnapshot); // <-- vigtigt

            upcomingMoviesSnapshotRepository.save(upcoming);
        }

        return savedUpcomingSnapshot;
    }


}
