package com.example.demo.repository;

import com.example.demo.models.MovieSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieSearchRepository  extends JpaRepository<MovieSearch, Long> {

}
