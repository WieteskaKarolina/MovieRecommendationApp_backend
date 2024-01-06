package com.example.demo.controllers;

import com.example.demo.models.Comment;
import com.example.demo.models.Movie;
import com.example.demo.models.MovieSearch;
import com.example.demo.services.*;
import org.hibernate.tool.schema.spi.CommandAcceptanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private WatchedService watchedService;

    @Autowired
    private WatchLaterService watchLaterService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MovieSearchService movieSearchService;

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
    public ResponseEntity<List<Movie>> searchMovies(@RequestParam String query, Principal principal) throws IOException, InterruptedException {
        try {
            String username = principal.getName();

            List<Movie> movies = new ArrayList<>(movieService.searchMovies(query));
            if (movies.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<Long> movieIds = new MovieIdExtractor().extractMovieIds(movies);
            movieSearchService.saveMovieSearch(query, movieIds, username);
            return new ResponseEntity<>(movies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getMovie")
    public ResponseEntity<Movie> getMovieDetailsById(@RequestParam String id) throws IOException, InterruptedException {
        try {
            Movie movie = movieService.getMovieDetailsById(id);
            if (movie == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(movie, HttpStatus.OK);
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

    @PostMapping("/ratings")
    public ResponseEntity<String> submitRating(@RequestBody RatingDto ratingDto, Principal principal) {
        String username = principal.getName();
        ratingService.submitRating(username, ratingDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/ratings/{movieId}")
    public ResponseEntity<Integer> getMovieRate(@PathVariable Long movieId, Principal principal) {
        String username = principal.getName();
        int userRate = ratingService.getMovieRate(username, movieId);
        return new ResponseEntity<>(userRate, HttpStatus.OK);
    }

    @PostMapping("/addMovieToWatched/{movieId}")
    public ResponseEntity<String> addMovieToWatched(@PathVariable Long movieId, Principal principal) {
        try {
            String username = principal.getName();
            watchedService.addWatchedMovie(username, movieId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/deleteMovieFromWatched/{movieId}")
    public ResponseEntity<String> deleteMovieFromWatched(@PathVariable Long movieId, Principal principal) {
        try {
            String username = principal.getName();
            watchedService.deleteWatchedMovie(username, movieId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getWatchedMovies")
    public ResponseEntity<List<Movie>> getWatchedMovies(Principal principal) {
        try {
            String username = principal.getName();
            List<Movie> movies = watchedService.getWatchedListForUser(username);
            return new ResponseEntity<>(movies, HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception and return a proper error response
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // toWatch

    @PostMapping("/addMovieToWatchLater/{movieId}")
    public ResponseEntity<String> addMovieToToWatch(@PathVariable Long movieId, Principal principal) {
        try {
            String username = principal.getName();
            watchLaterService.addMovieToWatchLater(username, movieId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/removeMovieFromWatchLater/{movieId}")
    public ResponseEntity<String> deleteMovieFromToWatch(@PathVariable Long movieId, Principal principal) {
        try {
            String username = principal.getName();
            watchLaterService.removeMovieFromWatchLater(username, movieId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getWatchLaterList")
    public ResponseEntity<List<Movie>> getToWatchMovies(Principal principal) {
        try {
            String username = principal.getName();
            List<Movie> movies = watchLaterService.getWatchLaterListForUser(username);
            return new ResponseEntity<>(movies, HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception and return a proper error response
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getCommentsForMovie")
    public ResponseEntity<List<Comment>> getCommentsForMovie(@RequestParam Long movieId) {
        try {
            List<Comment> comments = commentService.getCommentsForMovie(movieId);
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static class CommentRequest {
        private Long movieId;
        private String text;

        public Long getMovieId() {
            return movieId;
        }

        public String getText() {
            return text;
        }

    }


    @PostMapping("/addComment")
    public ResponseEntity<String> addComment(@RequestBody CommentRequest request, Principal principal) {
        try {
            String username = principal.getName();
            commentService.addComment(request.getMovieId(), request.getText(), username);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception and return a proper error response
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
