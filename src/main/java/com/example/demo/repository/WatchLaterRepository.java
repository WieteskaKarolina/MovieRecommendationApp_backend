package com.example.demo.repository;

import com.example.demo.models.Movie;
import com.example.demo.models.User;
import com.example.demo.models.WatchLater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchLaterRepository extends JpaRepository<WatchLater, Long> {
    List<WatchLater> findByUser(User user);
    WatchLater findByUserAndMovie(User user, Movie movie);
}
