package com.movieflix.movieflixAPI.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieflix.movieflixAPI.dto.MovieDto;
import com.movieflix.movieflixAPI.dto.MoviePageResponse;
import com.movieflix.movieflixAPI.exceptions.EmptyFileException;
import com.movieflix.movieflixAPI.services.MovieService;
import com.movieflix.movieflixAPI.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {
    private final MovieService movieService;
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    private MovieDto convertToMovieDto(String movieDto) throws JsonProcessingException {
        MovieDto movieDtoObject = new MovieDto();
        ObjectMapper mapper = new ObjectMapper();
        movieDtoObject = mapper.readValue(movieDto,MovieDto.class);
        return movieDtoObject;
    }

    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovie(@RequestPart String movieDto, @RequestPart MultipartFile file) throws IOException {
        if(file.isEmpty() || file == null) {
            throw new EmptyFileException("File is empty!");
        }
        MovieDto movie = convertToMovieDto(movieDto);
        return new ResponseEntity<>(movieService.addMovie(movie,file), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<MovieDto>> getAll(){
        return ResponseEntity.ok(movieService.getAllMovies());
    }
    @GetMapping("{id}")
    public ResponseEntity<MovieDto> getMovie(@PathVariable Integer id){
        return ResponseEntity.ok(movieService.getMovie(id));
    }

    @GetMapping("/pagination")
    public ResponseEntity<MoviePageResponse> getAllMoviesWithPagination(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ){
        return ResponseEntity.ok(movieService.getMoviesWithPagination(pageNumber,pageSize));
    }

    @GetMapping("/sorting")
    public ResponseEntity<MoviePageResponse> getAllMoviesWithPaginationAndSorting(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.DIR, required = false) String dir
    ){
        return ResponseEntity.ok(movieService.getMoviesWithPaginationAndSorting(pageNumber,pageSize,sortBy,dir));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Integer id, MultipartFile file, String movieDto) throws IOException {
        MovieDto mdto = convertToMovieDto(movieDto);
        return ResponseEntity.ok(movieService.updateMovie(id,mdto,file));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Integer id) throws IOException {
        return ResponseEntity.ok(movieService.deleteMovie(id));
    }
}
