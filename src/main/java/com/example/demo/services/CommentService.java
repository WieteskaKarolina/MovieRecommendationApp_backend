package com.example.demo.services;

import com.example.demo.models.Comment;
import com.example.demo.models.Movie;
import com.example.demo.models.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    public List<Comment> getCommentsForMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        List<Comment> comments = commentRepository.findByMovie(movie);
        if (comments.isEmpty())  return Collections.emptyList();
        return comments;
    }

    public void addComment(Long movieId, String text, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setMovie(movie);
        comment.setText(text);
        commentRepository.save(comment);
    }
}
