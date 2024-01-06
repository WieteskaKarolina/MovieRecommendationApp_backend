package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "movie")
@NoArgsConstructor
public class Movie {
    @Id
    @Column(name = "movie_id", nullable = false)
    private Long movie_id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "overview", length = 1000)
    private String overview;

    @Column(name = "vote_count")
    private Integer voteCount;

    @Column(name = "vote_average")
    private Double voteAverage;

    @Column(name = "popularity")
    private Double popularity;

    @Column(name = "release_date")
    private String releaseDate;

    @Column(name = "genre")
    private String genre;

    @Column(name = "poster_url")
    private String poster_url;

    @Column(name = "backdrop_url")
    private String backdrop_url;

    public Movie(Long movie_id, String title, String overview, Integer voteCount, Double voteAverage,
                 Double popularity, String releaseDate, String genre, String poster_url, String backdrop_url) {
        this.movie_id = movie_id;
        this.title = title;
        this.overview = overview;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.genre = genre;
        this.poster_url = poster_url;
        this.backdrop_url = backdrop_url;
    }
}
