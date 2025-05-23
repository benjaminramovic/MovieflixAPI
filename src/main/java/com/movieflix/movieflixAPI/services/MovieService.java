package com.movieflix.movieflixAPI.services;

import com.movieflix.movieflixAPI.dto.MovieDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {
    MovieDto addMovie(MovieDto movie, MultipartFile file) throws IOException;
    MovieDto getMovie(Integer id);
    List<MovieDto> getAllMovies();
    String deleteMovie(Integer id) throws IOException;
    MovieDto updateMovie(Integer id, MovieDto movie, MultipartFile file) throws IOException;
}
