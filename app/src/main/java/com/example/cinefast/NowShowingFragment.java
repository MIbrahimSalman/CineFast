package com.example.cinefast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NowShowingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_now_showing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupMovie(view,
                R.id.btnBookDarkKnight, R.id.btnTrailerDarkKnight,
                new Movie("The Dark Knight", "Action / Crime", "152 min", R.drawable.dark_knight,
                        "https://www.youtube.com/watch?v=EXeTwQWrcwY", false));

        setupMovie(view,
                R.id.btnBookInception, R.id.btnTrailerInception,
                new Movie("Inception", "Sci-Fi / Thriller", "148 min", R.drawable.inception,
                        "https://www.youtube.com/watch?v=YoHD9XEInc0", false));

        setupMovie(view,
                R.id.btnBookInterstellar, R.id.btnTrailerInterstellar,
                new Movie("Interstellar", "Sci-Fi / Drama", "169 min", R.drawable.interstellar,
                        "https://www.youtube.com/watch?v=zSWdZVtXT7E", false));

        setupMovie(view,
                R.id.btnBookShawshank, R.id.btnTrailerShawshank,
                new Movie("The Shawshank Redemption", "Drama", "142 min", R.drawable.shawshank,
                        "https://www.youtube.com/watch?v=6hB3S9bztOQ", false));
    }

    private void setupMovie(View root, int bookBtnId, int trailerBtnId, Movie movie) {
        root.findViewById(bookBtnId).setOnClickListener(v -> {
            if (getActivity() instanceof DrawerActivity) {
                ((DrawerActivity) getActivity()).navigateToSeatSelection(movie);
            }
        });

        root.findViewById(trailerBtnId).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailerUrl()));
            startActivity(intent);
        });
    }
}
