package com.example.demo.services;

import com.example.demo.models.Movie;
import com.example.demo.models.MovieGenres;
import com.example.demo.repository.MovieRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


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

    public void saveMovie(Movie movie) {
        movieRepository.save(movie);
    }

    public List<Movie> fetchPopularMovies() throws IOException, InterruptedException {
        URI uri = URI.create("https://api.themoviedb.org/3/movie/popular?language=en-US&page=1");
        String responseBody = getHttpRequestBody(uri);

        JSONObject responseObject = new JSONObject(responseBody);
        JSONArray movieArray = responseObject.getJSONArray("results");

        return getMovieListFromJSONObject(movieArray);
    }

    public List<Movie> fetchNowPlayingMovies() throws IOException, InterruptedException {
        URI uri = URI.create("https://api.themoviedb.org/3/movie/now_playing?language=en-US&page=1");
        String responseBody = getHttpRequestBody(uri);

        JSONObject responseObject = new JSONObject(responseBody);
        JSONArray movieArray = responseObject.getJSONArray("results");

        return getMovieListFromJSONObject(movieArray);
    }

    public List<Movie> fetchTopRatedMovies() throws IOException, InterruptedException {
        URI uri = URI.create("https://api.themoviedb.org/3/movie/top_rated?language=en-US&page=1");
        String responseBody = getHttpRequestBody(uri);

        JSONObject responseObject = new JSONObject(responseBody);
        JSONArray movieArray = responseObject.getJSONArray("results");

        return getMovieListFromJSONObject(movieArray);
    }


    public List<Movie> fetchUpcomingMovies() throws IOException, InterruptedException {
        URI uri = URI.create("https://api.themoviedb.org/3/movie/upcoming?language=en-US&page=1");
        String responseBody = getHttpRequestBody(uri);

        JSONObject responseObject = new JSONObject(responseBody);
        JSONArray movieArray = responseObject.getJSONArray("results");

        return getMovieListFromJSONObject(movieArray);
    }

    public List<Movie> fetchRecommendedMovies(String movieId) throws IOException, InterruptedException {
        URI uri = URI.create("https://api.themoviedb.org/3/movie/" + movieId + "/recommendations?language=en-US&page=1");

        String responseBody = getHttpRequestBody(uri);

        JSONObject responseObject = new JSONObject(responseBody);
        JSONArray movieArray = responseObject.getJSONArray("results");

        return getMovieListFromJSONObject(movieArray);
    }

    public List<Movie> fetchSimilarMovies(String movieId) throws IOException, InterruptedException {
        URI uri = URI.create("https://api.themoviedb.org/3/movie/" + movieId + "/similar?language=en-US&page=1");

        String responseBody = getHttpRequestBody(uri);

        JSONObject responseObject = new JSONObject(responseBody);
        JSONArray movieArray = responseObject.getJSONArray("results");

        return getMovieListFromJSONObject(movieArray);
    }


    public List<Movie> searchMovies(String query) throws IOException, InterruptedException {
        URI uri = URI.create("https://api.themoviedb.org/3/search/movie?query=" + query.replaceAll("%20", "%2B") + "&include_adult=false&language=en-US&page=1");
        String responseBody = getHttpRequestBody(uri);

        JSONObject responseObject = new JSONObject(responseBody);
        JSONArray movieArray = responseObject.getJSONArray("results");

        return getMovieListFromJSONObject(movieArray);
    }


    public List<Movie> genreFilter(String genre) throws IOException, InterruptedException {
        URI uri = URI.create("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&with_genres=" + MovieGenres.getGenreIdByName(genre));
        String responseBody = getHttpRequestBody(uri);

        JSONObject responseObject = new JSONObject(responseBody);
        JSONArray movieArray = responseObject.getJSONArray("results");

        return getMovieListFromJSONObject(movieArray);
    }

    public List<Movie> getMoviesDetailsByIds(List<String> ids) throws IOException, InterruptedException {
        List<Movie> movies = new ArrayList<>();

        for (String id : ids) {
            movies.add(getMovieDetailsById(id));
        }

        return movies;
    }

    public Movie getMovieDetailsById(String id) throws IOException, InterruptedException {
        URI uri = URI.create("https://api.themoviedb.org/3/movie/" + id + "?language=en-US");
        String responseBody = getHttpRequestBody(uri);
        JSONObject movieObject = new JSONObject(responseBody);
        Movie movie = getMovieFromJSONObject(movieObject);
        saveMovie(movie);
        return movie;
    }


    public Movie getMovieFromJSONObject(JSONObject movieObject) {
        String title = movieObject.getString("title");
        String overview = movieObject.optString("overview");
        Integer voteCount = movieObject.optInt("vote_count");
        Double voteAverage = movieObject.optDouble("vote_average");
        Double popularity = movieObject.optDouble("popularity");
        String releaseDate = movieObject.optString("release_date");
        Long apiId = movieObject.getLong("id");

        List<String> genres = new ArrayList<>();

        // Check if "genres" array is present
        if (movieObject.has("genres")) {
            JSONArray genresArray = movieObject.getJSONArray("genres");

            // Extract genre names from the array of objects
            for (int i = 0; i < genresArray.length(); i++) {
                JSONObject genreObject = genresArray.getJSONObject(i);
                String genreName = genreObject.getString("name");
                genres.add(genreName);
            }
        } else if (movieObject.has("genre_ids")) {
            // If "genres" array is not present, check for "genre_ids" array
            JSONArray genreIdsArray = movieObject.getJSONArray("genre_ids");

            // Map genre IDs to genre names using MovieGenres.getGenreById
            genres = IntStream.range(0, genreIdsArray.length())
                    .mapToObj(genreIdsArray::getInt)
                    .map(MovieGenres::getGenreById)
                    .toList();
        }
        String genresString = String.join("|", genres);
        String posterPathString = movieObject.optString("poster_path", null);
        String backdropPathString = movieObject.optString("backdrop_path", null);
        String TMDB_API_POSTERS = "https://image.tmdb.org/t/p/original";
        String EMPTY_POSTER = "https://static.thenounproject.com/png/318479-200.png";
        String posterPath = (posterPathString == null) ? EMPTY_POSTER : TMDB_API_POSTERS + posterPathString;
        String backdropPath = (backdropPathString == null) ? EMPTY_POSTER : TMDB_API_POSTERS + backdropPathString;

        return new Movie(apiId, title, overview, voteCount, voteAverage, popularity, releaseDate, genresString, posterPath, backdropPath);
    }

    public List<Movie> getMovieListFromJSONObject(JSONArray movieArray) {
        List<Movie> allMovies = new ArrayList<>();

        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movieObject = movieArray.getJSONObject(i);
            Movie movie = getMovieFromJSONObject(movieObject);
            saveMovie(movie);
            allMovies.add(movie);
        }
        return allMovies;
    }

    public String getHttpRequestBody(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("accept", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4YWMxZTE1MmRmYTNjZmRhOGFiMzc1ZTQ4MDFjYjk4YSIsInN1YiI6IjY1NThkZjhmMDgxNmM3MDEzN2VhN2EyZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.UbtVWJuS3IqCTIfYh4EqXywhvfdEjRNPxMIRsel4xPk")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
