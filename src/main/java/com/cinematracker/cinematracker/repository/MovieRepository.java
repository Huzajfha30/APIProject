package com.cinematracker.cinematracker.repository;

import com.cinematracker.cinematracker.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {


    Optional<Movie> findFirstByTitle(String title);
}
