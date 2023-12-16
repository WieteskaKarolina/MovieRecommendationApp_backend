package com.example.demo.services;

import com.example.demo.models.Movie;
import com.example.demo.models.Rating;
import com.example.demo.models.User;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.RatingRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    public void submitRating(String username, RatingDto ratingDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(ratingDto.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Rating existingRating = ratingRepository.findByUserAndMovie(user, movie);

        if (existingRating != null) {
            existingRating.setRating(ratingDto.getRating());
        } else {
            Rating newRating = new Rating();
            newRating.setUser(user);
            newRating.setMovie(movie);
            newRating.setRating(ratingDto.getRating());
            ratingRepository.save(newRating);
        }
    }

    public int getMovieRate(String username, Long movieId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Rating existingRating = ratingRepository.findByUserAndMovie(user, movie);

        return existingRating != null ? existingRating.getRating() : 0;
    }
}