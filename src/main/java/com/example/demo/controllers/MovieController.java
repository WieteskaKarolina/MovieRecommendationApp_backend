package com.example.demo.controllers;

import com.example.demo.models.Movie;
import com.example.demo.services.MovieService;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/topten")
    public ResponseEntity<List<Movie>> getTopTen() {
        try {
            List<Movie> movies = new ArrayList<>(movieService.fetchTopTenMovies());
            if (movies.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(movies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMovies(@RequestParam String query) throws IOException, InterruptedException {
        try {
            List<Movie> movies = new ArrayList<>(movieService.searchMovies(query));
            if (movies.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(movies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/genre")
    public ResponseEntity<List<Movie>> genreFilter(@RequestParam String query) throws IOException, InterruptedException {
        try {
            List<Movie> movies = new ArrayList<>(movieService.genreFilter(query));
            if (movies.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(movies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Movie> getMovieById(@PathVariable("id") long id) {
//        Optional<Movie> MovieData = movieRepository.findById(id);
//
//        if (MovieData.isPresent()) {
//            return new ResponseEntity<>(MovieData.get(), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

//    @PostMapping("/")
//    public ResponseEntity<Movie> createMovie(@RequestBody Movie Movie) {
//        try {
//            Movie _Movie = movieRepository
//                    .save(new Movie(Movie.getTitle(), Movie.getDescription(), false));
//            return new ResponseEntity<>(_Movie, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Movie> updateMovie(@PathVariable("id") long id, @RequestBody Movie Movie) {
//        Optional<Movie> MovieData = MovieRepository.findById(id);
//
//        if (MovieData.isPresent()) {
//            Movie _Movie = MovieData.get();
//            _Movie.setTitle(Movie.getTitle());
//            _Movie.setDescription(Movie.getDescription());
//            _Movie.setPublished(Movie.isPublished());
//            return new ResponseEntity<>(MovieRepository.save(_Movie), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @DeleteMapping("/Movies/{id}")
//    public ResponseEntity<HttpStatus> deleteMovie(@PathVariable("id") long id) {
//        try {
//            MovieRepository.deleteById(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
