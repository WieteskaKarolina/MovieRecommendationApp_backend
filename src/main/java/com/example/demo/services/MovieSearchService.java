package com.example.demo.services;

import com.example.demo.models.MovieSearch;
import com.example.demo.models.User;
import com.example.demo.repository.MovieSearchRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieSearchService {
    @Autowired
    private MovieSearchRepository movieSearchRepository;

    @Autowired
    private UserRepository userRepository;

    public void saveMovieSearch(String query, List<Long> movieIds, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MovieSearch movieSearch = new MovieSearch();
        movieSearch.setUser(user);
        movieSearch.setQuery(query);
        movieSearch.setMovieIds(movieIds);

        movieSearchRepository.save(movieSearch);
    }

}