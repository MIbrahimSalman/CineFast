package com.example.cinefast;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public interface OnBookSeatsListener {
        void onBookSeats(Movie movie);
    }

    private final List<Movie> movies;
    private final OnBookSeatsListener bookSeatsListener;

    public MovieAdapter(List<Movie> movies, OnBookSeatsListener bookSeatsListener) {
        this.movies = movies;
        this.bookSeatsListener = bookSeatsListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.imgPoster.setImageResource(movie.getPosterResId());
        holder.textName.setText(movie.getTitle());
        holder.textGenre.setText(movie.getGenre() + " • " + movie.getDuration());

        // Trailer button → implicit Intent to YouTube
        holder.btnTrailer.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailerUrl()));
            holder.itemView.getContext().startActivity(intent);
        });

        // Book Seats button → callback to fragment → NewMainActivity
        holder.btnBookSeats.setOnClickListener(v -> bookSeatsListener.onBookSeats(movie));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView textName, textGenre;
        Button btnTrailer, btnBookSeats;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgMoviePoster);
            textName = itemView.findViewById(R.id.textMovieName);
            textGenre = itemView.findViewById(R.id.textMovieGenre);
            btnTrailer = itemView.findViewById(R.id.btnTrailer);
            btnBookSeats = itemView.findViewById(R.id.btnBookSeats);
        }
    }
}
