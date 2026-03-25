package com.example.cinefast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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

        RecyclerView recycler = view.findViewById(R.id.recyclerNowShowing);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(
                "The Dark Knight",
                "Action / Crime",
                "152 min",
                R.drawable.dark_knight,
                "https://www.youtube.com/watch?v=EXeTwQWrcwY",
                false));
        movies.add(new Movie(
                "Inception",
                "Sci-Fi / Thriller",
                "148 min",
                R.drawable.inception,
                "https://www.youtube.com/watch?v=YoHD9XEInc0",
                false));
        movies.add(new Movie(
                "Interstellar",
                "Sci-Fi / Drama",
                "169 min",
                R.drawable.interstellar,
                "https://www.youtube.com/watch?v=zSWdZVtXT7E",
                false));

        MovieAdapter adapter = new MovieAdapter(movies, movie -> {
            if (getActivity() instanceof NewMainActivity) {
                ((NewMainActivity) getActivity()).navigateToSeatSelection(movie);
            }
        });
        recycler.setAdapter(adapter);
    }
}
