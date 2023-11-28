package com.example.demo.services;

import com.example.demo.models.Movie;
import com.example.demo.models.MovieGenres;
import com.example.demo.repository.MovieRepository;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


@Service
public class MovieService {

    private final RestTemplate restTemplate;
    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(RestTemplateBuilder restTemplateBuilder, MovieRepository movieRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<Movie> fetchTopTenMovies() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/popular?language=en-US&page=1"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YWMxZTE1MmRmYTNjZmRhOGFiMzc1ZTQ4MDFjYjk4YSIsInN1YiI6IjY1NThkZjhmMDgxNmM3MDEzN2VhN2EyZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.UbtVWJuS3IqCTIfYh4EqXywhvfdEjRNPxMIRsel4xPk")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        List<Movie> allMovies = new ArrayList<>();
        String responseBody = response.body();

        JSONObject json = new JSONObject(responseBody);
        JSONArray results = json.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject movie = results.getJSONObject(i);
            String title = movie.getString("title");
            String posterPath = movie.getString("poster_path");
            // JSONArray genreIds = movie.getJSONArray("genre_ids");


            allMovies.add(new Movie(title, "https://image.tmdb.org/t/p/original/"+posterPath));
        }
        return allMovies;
    }

    public List<Movie> searchMovies(String query) throws IOException, InterruptedException {
        query = query.replaceAll("%20", "%2B");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/search/movie?query="+query+"&include_adult=false&language=en-US&page=1"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YWMxZTE1MmRmYTNjZmRhOGFiMzc1ZTQ4MDFjYjk4YSIsInN1YiI6IjY1NThkZjhmMDgxNmM3MDEzN2VhN2EyZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.UbtVWJuS3IqCTIfYh4EqXywhvfdEjRNPxMIRsel4xPk")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        List<Movie> allMovies = new ArrayList<>();
        String responseBody = response.body();

        JSONObject json = new JSONObject(responseBody);
        JSONArray results = json.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject movie = results.getJSONObject(i);

            // Check for missing or null values
            String title = movie.optString("title", "Unknown Title");
            String posterPath = movie.optString("poster_path", null);
            if (posterPath == null) {
                posterPath = "https://static.thenounproject.com/png/318479-200.png";
            } else {
                posterPath = "https://image.tmdb.org/t/p/original" + posterPath;
            }

            allMovies.add(new Movie(title, posterPath));

        }
        return allMovies;
    }


    public List<Movie> genreFilter(String genre) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&with_genres="+MovieGenres.getGenreIdByName(genre)))
                .header("accept", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YWMxZTE1MmRmYTNjZmRhOGFiMzc1ZTQ4MDFjYjk4YSIsInN1YiI6IjY1NThkZjhmMDgxNmM3MDEzN2VhN2EyZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.UbtVWJuS3IqCTIfYh4EqXywhvfdEjRNPxMIRsel4xPk")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        List<Movie> allMovies = new ArrayList<>();
        String responseBody = response.body();

        JSONObject json = new JSONObject(responseBody);
        JSONArray results = json.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject movie = results.getJSONObject(i);
            String title = movie.getString("title");
            String posterPath = movie.getString("poster_path");
            // JSONArray genreIds = movie.getJSONArray("genre_ids");


            allMovies.add(new Movie(title, "https://image.tmdb.org/t/p/original/"+posterPath));
        }
        return allMovies;
    }
}
