package com.example.demo.controllers;


import com.example.demo.models.Movie;
import com.example.demo.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    public  ResponseEntity<Movie>  userAccess() {
        String[] genres = {"Science Fiction", "Adventure", "Action"};
        Movie movie = new Movie(609681L, "The Marvels", "Carol Danvers aka Captain Marvel has reclaimed her identity from the tyrannical Kree and taken revenge on the Supreme Intelligence. But unintended consequences see Carol shouldering the burden of a destabilized universe. When her duties send her to an anomalous wormhole linked to a Kree revolutionary, her powers become entangled with that of Jersey City super-fan Kamala Khan, aka Ms. Marvel, and Carol�s estranged niece, now S.A.B.E.R. astronaut Captain Monica Rambeau. Together, this unlikely trio must team up and learn to work in concert to save the universe.", 652, 6.6, 50.00, "2023-11-08", String.join("|", genres), "https://image.tmdb.org/t/p/original/Ag3D9qXjhJ2FUkrlJ0Cv1pgxqYQ.jpg", "https://image.tmdb.org/t/p/original/feSiISwgEpVzR1v3zv2n2AU4ANJ.jpg");
        return new ResponseEntity<>(movie, HttpStatus.OK);
        //return movieService.fetchTopTenMovies();

    }
}
