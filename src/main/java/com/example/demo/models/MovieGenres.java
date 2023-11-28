package com.example.demo.models;

import java.util.Map;

public class MovieGenres {

    private static final Map<Integer, String> GENRES_MAP;

    static {

        GENRES_MAP = Map.ofEntries(Map.entry(28, "Action"), Map.entry(12, "Adventure"),
                Map.entry(35, "Comedy"), Map.entry(80, "Crime"), Map.entry(99, "Documentary"),
                Map.entry(18, "Drama"), Map.entry(10751, "Family"), Map.entry(14, "Fantasy"),
                Map.entry(36, "History"), Map.entry(27, "Horror"), Map.entry(10402, "Music"),
                Map.entry(9648, "Mystery"), Map.entry(10749, "Romance"), Map.entry(878, "Science Fiction"),
                Map.entry(10770, "TV Movie"), Map.entry(53, "Thriller"), Map.entry(10752, "War"),
                Map.entry(37, "Western"), Map.entry(16, "Animation"));
    }

    public static String getGenreById(int genreId) {
        return GENRES_MAP.get(genreId);
    }

    public static Integer getGenreIdByName(String genreName) {
        for (Map.Entry<Integer, String> entry : GENRES_MAP.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(genreName)) {
                return entry.getKey();
            }
        }
        return null; // Genre not found
    }

    public static Map<Integer, String> getAllGenres() {
        return GENRES_MAP;
    }
}

