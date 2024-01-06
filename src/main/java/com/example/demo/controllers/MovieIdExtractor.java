package com.example.demo.controllers;

import com.example.demo.models.Movie;

import java.util.List;
import java.util.stream.Collectors;

public class MovieIdExtractor {
    public List<Long> extractMovieIds(List<Movie> movies) {
        return movies.stream()
                .map(Movie::getMovie_id) // Assuming 'getMovieId' is the getter for the movieId field
                .collect(Collectors.toList());
    }
}

