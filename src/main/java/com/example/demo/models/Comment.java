package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(columnDefinition = "TEXT")
    private String text;

    private LocalDateTime createdAt = LocalDateTime.now();
    private Long likes = 0L;

    @Transient
    private String userName;

    @PostLoad
    public void populateTransientFields() {
        if (user != null) {
            userName = user.getUsername();
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setText(String text) {
        this.text = text;
    }
}
