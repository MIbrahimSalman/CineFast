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
    private TextView textSeatCount;
    private TextView textTicketTotal;
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
        // Fix Hamburger Menu
        View btnMenu = view.findViewById(R.id.btnMenu);
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> {
                if (getActivity() instanceof DrawerActivity) {
                    ((DrawerActivity) getActivity()).openDrawer();
                }
            });
        }
    }

    // ── Now Showing Mode ──────────────────────────────────────────────────────

    private void setupNowShowingMode(GridLayout seatsGrid, View view) {
        view.findViewById(R.id.layoutNowShowingButtons).setVisibility(View.VISIBLE);
        view.findViewById(R.id.layoutComingSoonButtons).setVisibility(View.GONE);

        int totalRows = 8;
        int totalCols = 9;
        int gapColumn = 4;

        com.google.firebase.database.DatabaseReference ref = com.google.firebase.database.FirebaseDatabase.getInstance().getReference("all_bookings");
        
        // Query for bookings of this specific movie and date
        ref.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                Set<String> bookedSeats = new HashSet<>();
                for (com.google.firebase.database.DataSnapshot ds : snapshot.getChildren()) {
                    Booking b = ds.getValue(Booking.class);
                    if (b != null && b.getMovieTitle().equals(movie.getTitle()) && b.getMovieDate().equals(movie.getDate().split(", ")[0])) {
                        if (b.getSelectedSeats() != null) {
                            bookedSeats.addAll(b.getSelectedSeats());
                        }
                    }
                }
                
                seatsGrid.removeAllViews();
                populateSeats(seatsGrid, totalRows, totalCols, gapColumn, bookedSeats);
            }

            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {
                populateSeats(seatsGrid, totalRows, totalCols, gapColumn, new HashSet<>());
            }
        });

        btnBookSeats.setOnClickListener(v -> {
            if (selectedSeatTags.isEmpty()) {
                Toast.makeText(requireContext(), "Please select at least one seat.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Proceeding to booking summary...", Toast.LENGTH_SHORT).show();
                navigateToTicketSummary(0.0, 0, 0, 0, 0);
            }
        });

        btnProceedSnacks.setOnClickListener(v -> {
            if (selectedSeatTags.isEmpty()) {
                Toast.makeText(requireContext(), "Please select at least one seat.", Toast.LENGTH_SHORT).show();
            } else {
                if (getActivity() instanceof DrawerActivity) {
                    ArrayList<String> seatList = new ArrayList<>(selectedSeatTags);
                    double ticketPrice = 12.00 * selectedSeatTags.size();
                    ((DrawerActivity) getActivity()).navigateToSnacks(
                            movie.getTitle(), selectedSeatTags.size(), seatList, ticketPrice);
                }
            }
        });

        updateButtons();
    }

    private void populateSeats(GridLayout seatsGrid, int totalRows, int totalCols, int gapColumn, Set<String> bookedSeats) {
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
                    if (selectedSeatTags.contains(seatId)) {
                        seat.setBackgroundResource(R.drawable.ic_seat_selected_green);
                    } else {
                        seat.setBackgroundResource(R.drawable.ic_seat_available);
                    }
                    seat.setOnClickListener(v -> toggleSeat(v));
                }
                seatsGrid.addView(seat);
            }
        }
    }

    // ── Coming Soon Mode ──────────────────────────────────────────────────────

    private void setupComingSoonMode(View view) {
        view.findViewById(R.id.layoutNowShowingButtons).setVisibility(View.GONE);
        view.findViewById(R.id.layoutComingSoonButtons).setVisibility(View.VISIBLE);

        Button btnWatchTrailer = view.findViewById(R.id.btnWatchTrailer);
        if (btnWatchTrailer != null) {
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

        int count = selectedSeatTags.size();
        double total = 12.00 * count;
        if (textSeatCount != null)
            textSeatCount.setText("Selected: " + count + (count == 1 ? " seat" : " seats"));
        if (textTicketTotal != null)
            textTicketTotal.setText(String.format(java.util.Locale.getDefault(), "Total: $%.2f", total));
    }

    private void navigateToTicketSummary(double snacksTotal,
                                          int qtyPopcorn, int qtyNachos,
                                          int qtyDrink, int qtyCandy) {
        if (getActivity() instanceof DrawerActivity) {
            ArrayList<String> seatList = new ArrayList<>(selectedSeatTags);
            double ticketPrice = 12.00 * selectedSeatTags.size();
            double finalTotal = ticketPrice + snacksTotal;
            ((DrawerActivity) getActivity()).navigateToTicketSummary(
                    movie.getTitle(), selectedSeatTags.size(), seatList,
                    ticketPrice, snacksTotal, finalTotal,
                    qtyPopcorn, qtyNachos, qtyDrink, qtyCandy);
        }
    }
}
