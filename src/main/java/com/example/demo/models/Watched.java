package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "watched")
public class Watched {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "watched_id", nullable = false)
    private Long watchedId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
}
