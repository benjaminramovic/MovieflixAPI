package com.movieflix.movieflixAPI.services;

import com.movieflix.movieflixAPI.dto.MovieDto;
import com.movieflix.movieflixAPI.entities.Movie;
import com.movieflix.movieflixAPI.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final FileService fileService;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }

    @Override
    public MovieDto addMovie(MovieDto movie, MultipartFile file) throws IOException {
        //upload file
        if(Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))){
            throw new RuntimeException("File already exists. Please enter another file name!");
        }
        String uploadedFileName = fileService.uploadFile(path,file);
        movie.setPoster(uploadedFileName);
        Movie newMovie = new Movie(
          null,
          movie.getTitle(),
          movie.getDirector(),
          movie.getStudio(),
          movie.getMovieCast(),
          movie.getReleaseYear(),
          movie.getGenre(),
          movie.getPoster()
        );
        Movie savedMovie = movieRepository.save(newMovie);
        String posterUrl = baseUrl + "/file/" + uploadedFileName;

        return new MovieDto(
          savedMovie.getId(),
          savedMovie.getTitle(),
          savedMovie.getDirector(),
          savedMovie.getStudio(),
          savedMovie.getMovieCast(),
          savedMovie.getReleaseYear(),
          savedMovie.getGenre(),
          savedMovie.getPoster(),
          posterUrl
        );
    }

    @Override
    public MovieDto getMovie(Integer id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found!"));
        String posterUrl = baseUrl + "/file/" + movie.getPoster();
        return new MovieDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getGenre(),
                movie.getPoster(),
                posterUrl
        );

    }


    @Override
    public List<MovieDto> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        List<MovieDto> movieDtos = new ArrayList<>();

        for(Movie movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getGenre(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);
        }
        return movieDtos;
    }

    @Override
    public String deleteMovie(Integer id) throws IOException {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found!"));
        Files.deleteIfExists(Paths.get(path + File.separator + movie.getPoster()));
        movieRepository.delete(movie);

        return "Deleted movie with id " +movie.getId();
    }

    @Override
    public MovieDto updateMovie(Integer id, MovieDto movieDto, MultipartFile file) throws IOException {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found!"));
        String filename = movie.getPoster();
        if(file != null) {
            Files.deleteIfExists(Paths.get(path + File.separator + filename));
            filename = fileService.uploadFile(path,file);
        }
        movieDto.setPoster(filename);
        Movie updatedMovie = movieRepository.save(new Movie(
                movie.getId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getGenre(),
                movieDto.getPoster()
        ));
        String posterUrl = baseUrl + "/file/" + filename;

        return new MovieDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getGenre(),
                movie.getPoster(),
                posterUrl
        );
    }
}
