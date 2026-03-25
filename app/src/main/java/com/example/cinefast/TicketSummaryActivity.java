package com.example.cinefast;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Locale;

public class TicketSummaryActivity extends AppCompatActivity {

    private LinearLayout layoutTicketsContainer;
    private LinearLayout layoutSnacksContainer;
    private TextView textTotalAmount;
    private TextView textMovieTitle;
    private Button btnSendTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_summary);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        layoutTicketsContainer = findViewById(R.id.layoutTicketsContainer);
        layoutSnacksContainer = findViewById(R.id.layoutSnacksContainer);
        textTotalAmount = findViewById(R.id.textTotalAmount);
        textMovieTitle = findViewById(R.id.textMovieTitle);
        btnSendTicket = findViewById(R.id.btnSendTicket);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        // Menu button - No action for now
        findViewById(R.id.btnMenu).setOnClickListener(v -> {
        });

        Intent intent = getIntent();
        String movieTitle = intent.getStringExtra("MOVIE_TITLE");
        if (movieTitle != null) {
            textMovieTitle.setText(movieTitle);

            // Set Movie Poster
            android.widget.ImageView imgPoster = findViewById(R.id.imgPoster);
            if (movieTitle.equals("The Dark Knight")) {
                imgPoster.setImageResource(R.drawable.dark_knight);
            } else if (movieTitle.equals("Inception")) {
                imgPoster.setImageResource(R.drawable.inception);
            } else if (movieTitle.equals("Interstellar")) {
                imgPoster.setImageResource(R.drawable.interstellar);
            } else if (movieTitle.equals("The Shawshank Redemption")) {
                imgPoster.setImageResource(R.drawable.shawshank);
            }
        }

        ArrayList<String> selectedSeats = intent.getStringArrayListExtra("SELECTED_SEATS");
        double finalTotal = intent.getDoubleExtra("FINAL_TOTAL", 0.0);

        int qtyPopcorn = intent.getIntExtra("QTY_POPCORN", 0);
        int qtyNachos = intent.getIntExtra("QTY_NACHOS", 0);
        int qtyDrink = intent.getIntExtra("QTY_DRINK", 0);
        int qtyCandy = intent.getIntExtra("QTY_CANDY", 0);

        textTotalAmount.setText(String.format(Locale.getDefault(), "$%.2f USD", finalTotal));

        // Populate Tickets
        if (selectedSeats != null) {
            for (String seatTag : selectedSeats) {
                String[] parts = seatTag.split("_");
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);

                char rowChar = (char) ('A' + row);
                int seatNum = col + 1;

                String seatLabel = String.format(Locale.getDefault(), "Row %c, Seat %d", rowChar, seatNum);
                addRow(layoutTicketsContainer, seatLabel, "$12.00 USD");
            }
        }

        // Populate Snacks
        if (qtyPopcorn > 0)
            addRow(layoutSnacksContainer, qtyPopcorn + "x Popcorn",
                    String.format(Locale.getDefault(), "$%.2f USD", qtyPopcorn * 8.99));
        if (qtyNachos > 0)
            addRow(layoutSnacksContainer, qtyNachos + "x Nachos",
                    String.format(Locale.getDefault(), "$%.2f USD", qtyNachos * 7.99));
        if (qtyDrink > 0)
            addRow(layoutSnacksContainer, qtyDrink + "x Soft Drink",
                    String.format(Locale.getDefault(), "$%.2f USD", qtyDrink * 5.99));
        if (qtyCandy > 0)
            addRow(layoutSnacksContainer, qtyCandy + "x Candy Mix",
                    String.format(Locale.getDefault(), "$%.2f USD", qtyCandy * 6.99));

        if (qtyPopcorn == 0 && qtyNachos == 0 && qtyDrink == 0 && qtyCandy == 0) {
            findViewById(R.id.textSnacksHeader).setVisibility(View.GONE);
            layoutSnacksContainer.setVisibility(View.GONE);
        }

        btnSendTicket.setOnClickListener(v -> shareTicket(movieTitle, selectedSeats, finalTotal));
    }

    private void addRow(LinearLayout container, String label, String price) {
        LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, 0, 0, 16); // Bottom margin

        TextView textLabel = new TextView(this);
        textLabel.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)); // weight
                                                                                                                   // 1
        textLabel.setText(label);
        textLabel.setTextColor(Color.parseColor("#AAAAAA"));
        textLabel.setTextSize(16);

        TextView textPrice = new TextView(this);
        textPrice.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        textPrice.setText(price);
        textPrice.setTextColor(Color.WHITE);
        textPrice.setTextSize(16);
        textPrice.setTypeface(null, android.graphics.Typeface.BOLD);

        row.addView(textLabel);
        row.addView(textPrice);
        container.addView(row);
    }

    private void shareTicket(String movieTitle, ArrayList<String> seats, double total) {
        StringBuilder sb = new StringBuilder();
        sb.append("🎬 CineFAST Ticket\n\n");
        sb.append("Movie: ").append(movieTitle).append("\n");
        sb.append("Theater: Stars (90°Mall)\n");
        sb.append("Hall: 1st\n");
        sb.append("Date: 13.04.2025  |  Time: 22:15\n\n");

        sb.append("Seats:\n");
        if (seats != null) {
            for (String seatTag : seats) {
                String[] parts = seatTag.split("_");
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                char rowChar = (char) ('A' + row);
                int seatNum = (col > 4) ? col : col + 1;
                sb.append(String.format(Locale.getDefault(), "- Row %c, Seat %d\n", rowChar, seatNum));
            }
        }
        sb.append("\n");

        Intent intent = getIntent();
        int qtyPopcorn = intent.getIntExtra("QTY_POPCORN", 0);
        int qtyNachos = intent.getIntExtra("QTY_NACHOS", 0);
        int qtyDrink = intent.getIntExtra("QTY_DRINK", 0);
        int qtyCandy = intent.getIntExtra("QTY_CANDY", 0);

        if (qtyPopcorn > 0 || qtyNachos > 0 || qtyDrink > 0 || qtyCandy > 0) {
            sb.append("Snacks:\n");
            if (qtyPopcorn > 0)
                sb.append("- ").append(qtyPopcorn).append("x Popcorn\n");
            if (qtyNachos > 0)
                sb.append("- ").append(qtyNachos).append("x Nachos\n");
            if (qtyDrink > 0)
                sb.append("- ").append(qtyDrink).append("x Soft Drink\n");
            if (qtyCandy > 0)
                sb.append("- ").append(qtyCandy).append("x Candy Mix\n");
            sb.append("\n");
        }

        sb.append("Total Paid: ").append(String.format(Locale.getDefault(), "$%.2f USD", total)).append("\n\n");

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Movie Ticket: " + movieTitle);
        shareIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        startActivity(Intent.createChooser(shareIntent, "Share Ticket via"));
    }
}
