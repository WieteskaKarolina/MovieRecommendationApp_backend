package com.example.demo.services;

import com.example.demo.models.Movie;
import com.example.demo.models.User;
import com.example.demo.models.WatchLater;
import com.example.demo.models.Watched;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WatchLaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WatchLaterService {
    @Autowired
    private WatchLaterRepository watchLaterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getWatchLaterListForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<WatchLater> watchLaterList = watchLaterRepository.findByUser(user);

        return watchLaterList.stream()
                .map(WatchLater::getMovie)
                .collect(Collectors.toList());
    }

    public void addMovieToWatchLater(String username, Long movieId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        WatchLater watchLater = new WatchLater();
        watchLater.setUser(user);
        watchLater.setMovie(movie);
        watchLaterRepository.save(watchLater);
    }

    public void removeMovieFromWatchLater(String username, Long movieId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        WatchLater watchLater = watchLaterRepository.findByUserAndMovie(user, movie);

        watchLaterRepository.delete(watchLater);
    }

}

