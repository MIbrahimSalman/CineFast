package com.example.cinefast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Locale;

public class TicketSummaryFragment extends Fragment {

    private LinearLayout layoutTicketsContainer;
    private LinearLayout layoutSnacksContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ticket_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutTicketsContainer = view.findViewById(R.id.layoutTicketsContainer);
        layoutSnacksContainer = view.findViewById(R.id.layoutSnacksContainer);
        TextView textTotalAmount = view.findViewById(R.id.textTotalAmount);
        TextView textMovieTitle = view.findViewById(R.id.textMovieTitle);
        ImageView imgPoster = view.findViewById(R.id.imgPoster);

        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            if (getActivity() instanceof DrawerActivity) {
                ((DrawerActivity) getActivity()).navigateToHome();
            }
        });

        Bundle args = getArguments();
        if (args == null) return;

        String movieTitle = args.getString("MOVIE_TITLE", "");
        int seatCount = args.getInt("SEAT_COUNT", 0);
        ArrayList<String> selectedSeats = args.getStringArrayList("SELECTED_SEATS");
        double finalTotal = args.getDouble("FINAL_TOTAL", 0.0);
        double ticketTotal = args.getDouble("TICKET_TOTAL", 0.0);
        double snacksTotal = args.getDouble("SNACKS_TOTAL", 0.0);
        int qtyPopcorn = args.getInt("QTY_POPCORN", 0);
        int qtyNachos = args.getInt("QTY_NACHOS", 0);
        int qtyDrink = args.getInt("QTY_DRINK", 0);
        int qtyCandy = args.getInt("QTY_CANDY", 0);

        textMovieTitle.setText(movieTitle);
        textTotalAmount.setText(String.format(Locale.getDefault(), "$%.2f USD", finalTotal));

        // Set Date and Time from Movie object if available
        if (getActivity() instanceof DrawerActivity) {
            Movie movie = ((DrawerActivity) getActivity()).getCurrentMovie();
            if (movie != null && movie.getDate() != null) {
                String fullDate = movie.getDate();
                if (fullDate.contains(", ")) {
                    String[] parts = fullDate.split(", ");
                    TextView textMovieDate = view.findViewById(R.id.textMovieDate);
                    TextView textMovieTime = view.findViewById(R.id.textMovieTime);
                    if (textMovieDate != null) textMovieDate.setText(parts[0]);
                    if (textMovieTime != null) textMovieTime.setText(parts[1]);
                }
            }
        }

        // Poster — covers all 6 movies
        if (movieTitle.equals("The Dark Knight"))
            imgPoster.setImageResource(R.drawable.dark_knight);
        else if (movieTitle.equals("Inception"))
            imgPoster.setImageResource(R.drawable.inception);
        else if (movieTitle.equals("Interstellar"))
            imgPoster.setImageResource(R.drawable.interstellar);
        else if (movieTitle.equals("Oppenheimer"))
            imgPoster.setImageResource(R.drawable.interstellar);
        else if (movieTitle.equals("Dune: Part Two"))
            imgPoster.setImageResource(R.drawable.inception);
        else // The Shawshank Redemption
            imgPoster.setImageResource(R.drawable.shawshank);

        // Seats
        if (selectedSeats != null) {
            for (String seatTag : selectedSeats) {
                String[] parts = seatTag.split("_");
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                char rowChar = (char) ('A' + row);
                String seatLabel = String.format(Locale.getDefault(), "Row %c, Seat %d", rowChar, col + 1);
                addRow(layoutTicketsContainer, seatLabel, "$12.00 USD");
            }
        }

        // Snacks
        if (qtyPopcorn > 0) addRow(layoutSnacksContainer, qtyPopcorn + "x Popcorn",
                String.format(Locale.getDefault(), "$%.2f USD", qtyPopcorn * 8.99));
        if (qtyNachos > 0) addRow(layoutSnacksContainer, qtyNachos + "x Nachos",
                String.format(Locale.getDefault(), "$%.2f USD", qtyNachos * 7.99));
        if (qtyDrink > 0) addRow(layoutSnacksContainer, qtyDrink + "x Soft Drink",
                String.format(Locale.getDefault(), "$%.2f USD", qtyDrink * 5.99));
        if (qtyCandy > 0) addRow(layoutSnacksContainer, qtyCandy + "x Candy Mix",
                String.format(Locale.getDefault(), "$%.2f USD", qtyCandy * 6.99));

        if (qtyPopcorn == 0 && qtyNachos == 0 && qtyDrink == 0 && qtyCandy == 0) {
            view.findViewById(R.id.textSnacksHeader).setVisibility(View.GONE);
            layoutSnacksContainer.setVisibility(View.GONE);
        }

        // Save to SharedPreferences
        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("CineFastPrefs", android.content.Context.MODE_PRIVATE);
        prefs.edit()
                .putString("last_movie", movieTitle)
                .putInt("last_seats", seatCount)
                .putFloat("last_price", (float) finalTotal)
                .apply();

        // Confirm Booking button
        view.findViewById(R.id.btnSendTicket).setOnClickListener(v -> {
            confirmBooking(movieTitle, seatCount, selectedSeats, finalTotal);
        });

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

    private void addRow(LinearLayout container, String label, String price) {
        LinearLayout row = new LinearLayout(requireContext());
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, 0, 0, 16);

        TextView textLabel = new TextView(requireContext());
        textLabel.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        textLabel.setText(label);
        textLabel.setTextColor(Color.parseColor("#AAAAAA"));
        textLabel.setTextSize(16);

        TextView textPrice = new TextView(requireContext());
        textPrice.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textPrice.setText(price);
        textPrice.setTextColor(Color.WHITE);
        textPrice.setTextSize(16);
        textPrice.setTypeface(null, android.graphics.Typeface.BOLD);

        row.addView(textLabel);
        row.addView(textPrice);
        container.addView(row);
    }

    private void confirmBooking(String movieTitle, int seatCount, ArrayList<String> selectedSeats, double finalTotal) {
        com.google.firebase.auth.FirebaseUser user = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            android.widget.Toast.makeText(requireContext(), "You must be logged in to book.", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }
        String uid = user.getUid();

        com.google.firebase.database.FirebaseDatabase db = com.google.firebase.database.FirebaseDatabase.getInstance();
        com.google.firebase.database.DatabaseReference userRef = db.getReference("bookings").child(uid);
        com.google.firebase.database.DatabaseReference globalRef = db.getReference("all_bookings");
        
        String bookingId = userRef.push().getKey();

        if (bookingId != null) {
            long showTimestamp = System.currentTimeMillis(); // fallback
            String mDate = "";
            String mTime = "";
            
            if (getActivity() instanceof DrawerActivity) {
                Movie currentMovie = ((DrawerActivity) getActivity()).getCurrentMovie();
                if (currentMovie != null && currentMovie.getDate() != null) {
                    try {
                        String fullDate = currentMovie.getDate();
                        if (fullDate.contains(", ")) {
                            String[] parts = fullDate.split(", ");
                            mDate = parts[0];
                            mTime = parts[1];
                        }
                        
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy, HH:mm", java.util.Locale.getDefault());
                        java.util.Date showDate = sdf.parse(currentMovie.getDate());
                        if (showDate != null) {
                            showTimestamp = showDate.getTime();
                        }
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            
            Booking booking = new Booking(bookingId, uid, movieTitle, seatCount, selectedSeats, finalTotal, System.currentTimeMillis(), showTimestamp, mDate, mTime);
            
            // Save to user node
            userRef.child(bookingId).setValue(booking);
            // Save to global node for seat occupancy checking
            globalRef.child(bookingId).setValue(booking).addOnSuccessListener(aVoid -> {
                android.widget.Toast.makeText(requireContext(), "Booking confirmed successfully!", android.widget.Toast.LENGTH_SHORT).show();
                if (getActivity() instanceof DrawerActivity) {
                    ((DrawerActivity) getActivity()).navigateToHome();
                }
            }).addOnFailureListener(e -> {
                android.widget.Toast.makeText(requireContext(), "Failed: " + e.getMessage(), android.widget.Toast.LENGTH_LONG).show();
                android.util.Log.e("FirebaseBooking", "Error saving booking", e);
            });
        }
    }
}
