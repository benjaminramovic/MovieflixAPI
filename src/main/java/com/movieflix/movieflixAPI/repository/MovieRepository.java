package com.movieflix.movieflixAPI.repository;

import com.movieflix.movieflixAPI.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
}
