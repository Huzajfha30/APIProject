package com.cinematracker.cinematracker.service;

import com.cinematracker.cinematracker.dto.TMDBMovieDto;
import com.cinematracker.cinematracker.dto.TMDBResponseDto;
import com.cinematracker.cinematracker.model.Movie;
import com.cinematracker.cinematracker.model.MovieSnapshots;
import com.cinematracker.cinematracker.model.Snapshots;
import com.cinematracker.cinematracker.repository.MovieSnapshotRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
            movieSnapshot.setVotes(movieDto.getVotes());

            save(movieSnapshot);
        }
    }

    public Snapshots fetchAndSaveMoviesAsSnapshot() {
        Snapshots snapshots = snapshotService.createNewSnapshot(LocalDateTime.now());
        TMDBResponseDto tmdbResponseDto = tmdbService.getNowPlayingMovies();
        saveMoviesToSnapshot(tmdbResponseDto, snapshots);

        return snapshots;
    }
}

