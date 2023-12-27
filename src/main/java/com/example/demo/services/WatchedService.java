package com.example.demo.services;

import com.example.demo.models.Movie;
import com.example.demo.models.User;
import com.example.demo.models.Watched;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WatchedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WatchedService {
    @Autowired
    private WatchedRepository watchedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getWatchedListForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Watched> watchedList = watchedRepository.findByUser(user);

        return watchedList.stream()
                .map(Watched::getMovie)
                .collect(Collectors.toList());
    }

    public void addWatchedMovie(String username, Long movieId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Watched watched = new Watched();
        watched.setUser(user);
        watched.setMovie(movie);
        watchedRepository.save(watched);
    }

    public void deleteWatchedMovie(String username, Long movieId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Watched watched = watchedRepository.findByUserAndMovie(user, movie);

        watchedRepository.delete(watched);
    }


}

