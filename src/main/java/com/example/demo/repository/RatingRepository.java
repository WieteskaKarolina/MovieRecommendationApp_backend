package com.example.demo.repository;

import com.example.demo.models.Movie;
import com.example.demo.models.Rating;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Rating findByUserAndMovie(User user, Movie movie);
}

