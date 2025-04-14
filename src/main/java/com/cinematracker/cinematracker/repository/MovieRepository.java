package com.cinematracker.cinematracker.repository;

import com.cinematracker.cinematracker.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {


}
