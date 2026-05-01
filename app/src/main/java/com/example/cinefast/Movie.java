package com.example.cinefast;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    public static List<Movie> loadMoviesFromJson(Context context, boolean comingSoonFilter) {
        List<Movie> movies = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("movies.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonStr = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                boolean isComingSoon = obj.getBoolean("isComingSoon");

                if (isComingSoon == comingSoonFilter) {
                    String title = obj.getString("title");
                    String genre = obj.getString("genre");
                    String duration = obj.getString("duration");
                    String posterName = obj.getString("posterName");
                    String trailerUrl = obj.getString("trailerUrl");

                    int resId = context.getResources().getIdentifier(posterName, "drawable", context.getPackageName());
                    if (resId == 0) {
                        resId = R.drawable.movie_img; // fallback
                    }

                    movies.add(new Movie(title, genre, duration, resId, trailerUrl, isComingSoon));
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
