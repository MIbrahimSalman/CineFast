package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SnacksActivity extends AppCompatActivity {

    private int qtyPopcorn = 0;
    private int qtyNachos = 0;
    private int qtyDrink = 0;
    private int qtyCandy = 0;

    private final double PRICE_POPCORN = 8.99;
    private final double PRICE_NACHOS = 7.99;
    private final double PRICE_DRINK = 5.99;
    private final double PRICE_CANDY = 6.99;

    private TextView textQtyPopcorn, textQtyNachos, textQtyDrink, textQtyCandy;
    private Button btnConfirm;

    private String movieTitle;
    private int seatCount;
    private double ticketPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snacks);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        movieTitle = getIntent().getStringExtra("MOVIE_TITLE");
        seatCount = getIntent().getIntExtra("SEAT_COUNT", 0);
        ticketPrice = getIntent().getDoubleExtra("TICKET_PRICE", 0.0);
        java.util.ArrayList<String> selectedSeats = getIntent().getStringArrayListExtra("SELECTED_SEATS");
        textQtyPopcorn = findViewById(R.id.qtyPopcorn);
        textQtyNachos = findViewById(R.id.qtyNachos);
        textQtyDrink = findViewById(R.id.qtyDrink);
        textQtyCandy = findViewById(R.id.qtyCandy);
        btnConfirm = findViewById(R.id.btnConfirm);

        findViewById(R.id.btnPlusPopcorn).setOnClickListener(v -> {
            qtyPopcorn++;
            updateUI();
        });
        findViewById(R.id.btnMinusPopcorn).setOnClickListener(v -> {
            if (qtyPopcorn > 0) {
                qtyPopcorn--;
                updateUI();
            }
        });

        findViewById(R.id.btnPlusNachos).setOnClickListener(v -> {
            qtyNachos++;
            updateUI();
        });
        findViewById(R.id.btnMinusNachos).setOnClickListener(v -> {
            if (qtyNachos > 0) {
                qtyNachos--;
                updateUI();
            }
        });

        findViewById(R.id.btnPlusDrink).setOnClickListener(v -> {
            qtyDrink++;
            updateUI();
        });
        findViewById(R.id.btnMinusDrink).setOnClickListener(v -> {
            if (qtyDrink > 0) {
                qtyDrink--;
                updateUI();
            }
        });

        findViewById(R.id.btnPlusCandy).setOnClickListener(v -> {
            qtyCandy++;
            updateUI();
        });
        findViewById(R.id.btnMinusCandy).setOnClickListener(v -> {
            if (qtyCandy > 0) {
                qtyCandy--;
                updateUI();
            }
        });

        btnConfirm.setOnClickListener(v -> {
            double snacksTotal = calculateSnacksTotal();
            double finalTotal = ticketPrice + snacksTotal;

            Intent intent = new Intent(SnacksActivity.this, TicketSummaryActivity.class);
            intent.putExtra("MOVIE_TITLE", movieTitle);
            intent.putExtra("SEAT_COUNT", seatCount);
            intent.putExtra("TICKET_TOTAL", ticketPrice);
            intent.putExtra("SNACKS_TOTAL", snacksTotal);
            intent.putExtra("FINAL_TOTAL", finalTotal);

            intent.putStringArrayListExtra("SELECTED_SEATS", selectedSeats);
            intent.putExtra("QTY_POPCORN", qtyPopcorn);
            intent.putExtra("QTY_NACHOS", qtyNachos);
            intent.putExtra("QTY_DRINK", qtyDrink);
            intent.putExtra("QTY_CANDY", qtyCandy);

            startActivity(intent);
        });

        updateUI();
    }

    private void updateUI() {
        textQtyPopcorn.setText(String.valueOf(qtyPopcorn));
        textQtyNachos.setText(String.valueOf(qtyNachos));
        textQtyDrink.setText(String.valueOf(qtyDrink));
        textQtyCandy.setText(String.valueOf(qtyCandy));
    }

    private double calculateSnacksTotal() {
        return (qtyPopcorn * PRICE_POPCORN) +
                (qtyNachos * PRICE_NACHOS) +
                (qtyDrink * PRICE_DRINK) +
                (qtyCandy * PRICE_CANDY);
    }
}
