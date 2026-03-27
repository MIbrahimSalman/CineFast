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
            if (getActivity() instanceof NewMainActivity) {
                ((NewMainActivity) getActivity()).navigateToHome();
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

        // Send Ticket button
        view.findViewById(R.id.btnSendTicket).setOnClickListener(v ->
                shareTicket(movieTitle, selectedSeats, finalTotal,
                        qtyPopcorn, qtyNachos, qtyDrink, qtyCandy));
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

    private void shareTicket(String movieTitle, ArrayList<String> seats, double total,
                              int qtyPopcorn, int qtyNachos, int qtyDrink, int qtyCandy) {
        StringBuilder sb = new StringBuilder();
        sb.append("🎬 CineFAST Ticket\n\nMovie: ").append(movieTitle).append("\n");
        sb.append("Theater: Stars (90°Mall)\nHall: 1st\nDate: 13.04.2025  |  Time: 22:15\n\nSeats:\n");
        if (seats != null) {
            for (String seatTag : seats) {
                String[] parts = seatTag.split("_");
                char rowChar = (char) ('A' + Integer.parseInt(parts[0]));
                int seatNum = Integer.parseInt(parts[1]) + 1;
                sb.append(String.format(Locale.getDefault(), "- Row %c, Seat %d\n", rowChar, seatNum));
            }
        }
        if (qtyPopcorn > 0 || qtyNachos > 0 || qtyDrink > 0 || qtyCandy > 0) {
            sb.append("\nSnacks:\n");
            if (qtyPopcorn > 0) sb.append("- ").append(qtyPopcorn).append("x Popcorn\n");
            if (qtyNachos > 0) sb.append("- ").append(qtyNachos).append("x Nachos\n");
            if (qtyDrink > 0) sb.append("- ").append(qtyDrink).append("x Soft Drink\n");
            if (qtyCandy > 0) sb.append("- ").append(qtyCandy).append("x Candy Mix\n");
        }
        sb.append("\nTotal Paid: ").append(String.format(Locale.getDefault(), "$%.2f USD", total));

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Movie Ticket: " + movieTitle);
        shareIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        startActivity(Intent.createChooser(shareIntent, "Share Ticket via"));
    }
}
