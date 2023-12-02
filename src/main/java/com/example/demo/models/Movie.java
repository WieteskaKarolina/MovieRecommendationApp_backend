package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "movie")
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id", nullable = false)
    private Long movie_id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "genre")
    private String genre;

    @Column(name = "poster_url")
    private String poster_url;

    @Column(name = "backdrop_url")
    private String backdrop_url;

    @Column(name = "api_id")
    private Long api_id;

    public Movie(String title, String poster_url, Long api_id, String backdrop_url) {
        this.title = title;
        this.poster_url = poster_url;
        this.api_id = api_id;
        this.backdrop_url = backdrop_url;
    }
}
