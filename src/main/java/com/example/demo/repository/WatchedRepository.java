package com.example.demo.repository;

import com.example.demo.models.Movie;
import com.example.demo.models.User;
import com.example.demo.models.Watched;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WatchedRepository extends JpaRepository<Watched, Long> {
    List<Watched> findByUser(User user);
    Watched findByUserAndMovie(User user, Movie movie);
}
