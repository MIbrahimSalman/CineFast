package com.example.cinefast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SeatSelectionFragment extends Fragment {

    private Set<String> selectedSeatTags = new HashSet<>();
    private Button btnProceedSnacks;
    private Button btnBookSeats;
    private Movie movie;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seat_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable("MOVIE");
        }

        TextView textMovieTitle = view.findViewById(R.id.textMovieTitle);
        if (movie != null) {
            textMovieTitle.setText(movie.getTitle());
        }

        btnProceedSnacks = view.findViewById(R.id.btnProceedSnacks);
        btnBookSeats = view.findViewById(R.id.btnBookSeats);
        GridLayout seatsGrid = view.findViewById(R.id.seatsGrid);

        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        boolean isComingSoon = movie != null && movie.isComingSoon();

        if (isComingSoon) {
            setupComingSoonMode(view);
        } else {
            setupNowShowingMode(seatsGrid, view);
        }
    }

    // ── Now Showing Mode ──────────────────────────────────────────────────────

    private void setupNowShowingMode(GridLayout seatsGrid, View view) {
        btnProceedSnacks.setVisibility(View.VISIBLE);
        btnBookSeats.setVisibility(View.VISIBLE);

        Button btnComingSoon = view.findViewById(R.id.btnComingSoon);
        Button btnWatchTrailer = view.findViewById(R.id.btnWatchTrailer);
        if (btnComingSoon != null) btnComingSoon.setVisibility(View.GONE);
        if (btnWatchTrailer != null) btnWatchTrailer.setVisibility(View.GONE);

        int totalRows = 8;
        int totalCols = 9;
        int gapColumn = 4;

        Set<String> bookedSeats = new HashSet<>();
        bookedSeats.add("1_2"); bookedSeats.add("1_6");
        bookedSeats.add("3_3"); bookedSeats.add("3_5");
        bookedSeats.add("5_1"); bookedSeats.add("6_2");
        bookedSeats.add("6_7"); bookedSeats.add("7_3");

        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < totalCols; col++) {
                if (col == gapColumn) {
                    View gap = new View(requireContext());
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = 60; params.height = 100;
                    gap.setLayoutParams(params);
                    seatsGrid.addView(gap);
                    continue;
                }
                View seat = new View(requireContext());
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 90; params.height = 90;
                params.setMargins(8, 12, 8, 12);
                seat.setLayoutParams(params);

                if ((row == 0 || row == totalRows - 1) && (col == 0 || col == totalCols - 1)) {
                    seat.setVisibility(View.INVISIBLE);
                    seatsGrid.addView(seat);
                    continue;
                }

                String seatId = row + "_" + col;
                seat.setTag(seatId);

                if (bookedSeats.contains(seatId)) {
                    seat.setBackgroundResource(R.drawable.ic_seat_booked);
                    seat.setEnabled(false);
                } else {
                    seat.setBackgroundResource(R.drawable.ic_seat_available);
                    seat.setOnClickListener(v -> toggleSeat(v));
                }
                seatsGrid.addView(seat);
            }
        }

        btnBookSeats.setOnClickListener(v -> {
            if (selectedSeatTags.isEmpty()) {
                Toast.makeText(requireContext(), "Please select at least one seat.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Booking Confirmed!", Toast.LENGTH_SHORT).show();
                navigateToTicketSummary(0.0, 0, 0, 0, 0);
            }
        });

        btnProceedSnacks.setOnClickListener(v -> {
            if (selectedSeatTags.isEmpty()) {
                Toast.makeText(requireContext(), "Please select at least one seat.", Toast.LENGTH_SHORT).show();
            } else {
                if (getActivity() instanceof NewMainActivity) {
                    ArrayList<String> seatList = new ArrayList<>(selectedSeatTags);
                    double ticketPrice = 12.00 * selectedSeatTags.size();
                    ((NewMainActivity) getActivity()).navigateToSnacks(
                            movie.getTitle(), selectedSeatTags.size(), seatList, ticketPrice);
                }
            }
        });

        updateButtons();
    }

    // ── Coming Soon Mode ──────────────────────────────────────────────────────

    private void setupComingSoonMode(View view) {
        btnProceedSnacks.setVisibility(View.GONE);
        btnBookSeats.setVisibility(View.GONE);

        Button btnComingSoon = view.findViewById(R.id.btnComingSoon);
        Button btnWatchTrailer = view.findViewById(R.id.btnWatchTrailer);
        if (btnComingSoon != null) {
            btnComingSoon.setVisibility(View.VISIBLE);
            btnComingSoon.setEnabled(false);
            btnComingSoon.setAlpha(0.5f);
        }
        if (btnWatchTrailer != null) {
            btnWatchTrailer.setVisibility(View.VISIBLE);
            btnWatchTrailer.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(
                        android.content.Intent.ACTION_VIEW,
                        android.net.Uri.parse(movie.getTrailerUrl()));
                startActivity(intent);
            });
        }

        // Disable seat grid by overlaying an invisible view intercepting touches
        GridLayout seatsGrid = view.findViewById(R.id.seatsGrid);
        int totalRows = 8, totalCols = 9, gapColumn = 4;
        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < totalCols; col++) {
                if (col == gapColumn) {
                    View gap = new View(requireContext());
                    GridLayout.LayoutParams p = new GridLayout.LayoutParams();
                    p.width = 60; p.height = 100; gap.setLayoutParams(p);
                    seatsGrid.addView(gap); continue;
                }
                View seat = new View(requireContext());
                GridLayout.LayoutParams p = new GridLayout.LayoutParams();
                p.width = 90; p.height = 90; p.setMargins(8, 12, 8, 12);
                seat.setLayoutParams(p);
                if ((row == 0 || row == totalRows - 1) && (col == 0 || col == totalCols - 1)) {
                    seat.setVisibility(View.INVISIBLE); seatsGrid.addView(seat); continue;
                }
                seat.setBackgroundResource(R.drawable.ic_seat_booked);
                seat.setEnabled(false);
                seatsGrid.addView(seat);
            }
        }
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private void toggleSeat(View seat) {
        String seatTag = (String) seat.getTag();
        if (selectedSeatTags.contains(seatTag)) {
            selectedSeatTags.remove(seatTag);
            seat.setBackgroundResource(R.drawable.ic_seat_available);
        } else {
            selectedSeatTags.add(seatTag);
            seat.setBackgroundResource(R.drawable.ic_seat_selected_green);
        }
        updateButtons();
    }

    private void updateButtons() {
        boolean hasSeats = !selectedSeatTags.isEmpty();
        btnProceedSnacks.setEnabled(hasSeats);
        btnProceedSnacks.setAlpha(hasSeats ? 1.0f : 0.5f);
    }

    private void navigateToTicketSummary(double snacksTotal,
                                          int qtyPopcorn, int qtyNachos,
                                          int qtyDrink, int qtyCandy) {
        if (getActivity() instanceof NewMainActivity) {
            ArrayList<String> seatList = new ArrayList<>(selectedSeatTags);
            double ticketPrice = 12.00 * selectedSeatTags.size();
            double finalTotal = ticketPrice + snacksTotal;
            ((NewMainActivity) getActivity()).navigateToTicketSummary(
                    movie.getTitle(), selectedSeatTags.size(), seatList,
                    ticketPrice, snacksTotal, finalTotal,
                    qtyPopcorn, qtyNachos, qtyDrink, qtyCandy);
        }
    }
}
