package com.example.cinefast;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;
import java.util.Set;

public class SeatSelectionActivity extends AppCompatActivity {

    private Set<String> selectedSeatTags = new HashSet<>();
    private Button btnProceedSnacks;
    private Button btnBookSeats;
    private String movieTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        movieTitle = getIntent().getStringExtra("MOVIE_TITLE");
        TextView textMovieTitle = findViewById(R.id.textMovieTitle);
        if (movieTitle != null) {
            textMovieTitle.setText(movieTitle);
        }

        btnProceedSnacks = findViewById(R.id.btnProceedSnacks);
        btnBookSeats = findViewById(R.id.btnBookSeats);
        GridLayout seatsGrid = findViewById(R.id.seatsGrid);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        int totalRows = 8;
        int totalCols = 9;
        int gapColumn = 4;

        Set<String> bookedSeats = new HashSet<>();
        bookedSeats.add("1_2");
        bookedSeats.add("1_6");
        bookedSeats.add("3_3");
        bookedSeats.add("3_5");
        bookedSeats.add("5_1");
        bookedSeats.add("6_2");
        bookedSeats.add("6_7");
        bookedSeats.add("7_3");

        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < totalCols; col++) {
                if (col == gapColumn) {
                    View gap = new View(this);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = 60;
                    params.height = 100;
                    gap.setLayoutParams(params);
                    seatsGrid.addView(gap);
                    continue;
                }

                View seat = new View(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 90;
                params.height = 90;
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
                Toast.makeText(this, "Please select at least one seat.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, TicketSummaryActivity.class);
                intent.putExtra("MOVIE_TITLE", movieTitle);
                intent.putExtra("SEAT_COUNT", selectedSeatTags.size());
                double ticketPrice = 12.00 * selectedSeatTags.size();
                intent.putExtra("TICKET_TOTAL", ticketPrice);
                intent.putExtra("SNACKS_TOTAL", 0.0);
                intent.putExtra("FINAL_TOTAL", ticketPrice);

                java.util.ArrayList<String> seatList = new java.util.ArrayList<>(selectedSeatTags);
                intent.putStringArrayListExtra("SELECTED_SEATS", seatList);

                intent.putExtra("QTY_POPCORN", 0);
                intent.putExtra("QTY_NACHOS", 0);
                intent.putExtra("QTY_DRINK", 0);
                intent.putExtra("QTY_CANDY", 0);

                startActivity(intent);
            }
        });

        btnProceedSnacks.setOnClickListener(v -> {
            Intent intent = new Intent(this, SnacksActivity.class);
            intent.putExtra("MOVIE_TITLE", movieTitle);
            intent.putExtra("SEAT_COUNT", selectedSeatTags.size());
            intent.putExtra("TICKET_PRICE", 12.00 * selectedSeatTags.size());

            java.util.ArrayList<String> seatList = new java.util.ArrayList<>(selectedSeatTags);
            intent.putStringArrayListExtra("SELECTED_SEATS", seatList);

            startActivity(intent);
        });

        updateButtons();
    }

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
}
