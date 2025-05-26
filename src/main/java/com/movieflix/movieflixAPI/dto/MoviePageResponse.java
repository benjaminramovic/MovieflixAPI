package com.movieflix.movieflixAPI.dto;

import java.util.List;

public record MoviePageResponse(
        List<MovieDto> movieList,
        Integer pageNumber,
        Integer pageSize,
        int totalElements,
        int totalPages,
        boolean isLast
) {
}
