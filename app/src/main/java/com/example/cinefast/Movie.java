package com.example.cinefast;

import java.io.Serializable;

public class Movie implements Serializable {

    private String title;
    private String genre;
    private String duration;
    private int posterResId;
    private String trailerUrl;
    private boolean isComingSoon;

    public Movie(String title, String genre, String duration, int posterResId, String trailerUrl, boolean isComingSoon) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.posterResId = posterResId;
        this.trailerUrl = trailerUrl;
        this.isComingSoon = isComingSoon;
    }

    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getDuration() { return duration; }
    public int getPosterResId() { return posterResId; }
    public String getTrailerUrl() { return trailerUrl; }
    public boolean isComingSoon() { return isComingSoon; }
}
