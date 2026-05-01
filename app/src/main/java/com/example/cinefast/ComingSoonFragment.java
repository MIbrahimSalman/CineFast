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

import java.util.List;

public class ComingSoonFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coming_soon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recycler = view.findViewById(R.id.recyclerComingSoon);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Load movies from JSON
        List<Movie> movies = Movie.loadMoviesFromJson(requireContext(), true);

        MovieAdapter adapter = new MovieAdapter(movies, movie -> {
            if (getActivity() instanceof DrawerActivity) {
                ((DrawerActivity) getActivity()).navigateToSeatSelection(movie);
            }
        });
        recycler.setAdapter(adapter);
    }
}
