package com.example.cinefast;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Hide the action bar if present
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setupDateToggle();

        setupMovieClick(R.id.btnBookDarkKnight, "The Dark Knight");
        setupMovieClick(R.id.btnBookInception, "Inception");
        setupMovieClick(R.id.btnBookInterstellar, "Interstellar");
        setupMovieClick(R.id.btnBookShawshank, "The Shawshank Redemption");

        setupTrailerClick(R.id.btnTrailerDarkKnight, "https://www.youtube.com/watch?v=EXeTwQWrcwY");
        setupTrailerClick(R.id.btnTrailerInception, "https://www.youtube.com/watch?v=YoHD9XEInc0");
        setupTrailerClick(R.id.btnTrailerInterstellar, "https://www.youtube.com/watch?v=zSWdZVtXT7E");
        setupTrailerClick(R.id.btnTrailerShawshank, "https://www.youtube.com/watch?v=6hB3S9bztOQ");
    }

    private void setupTrailerClick(int buttonId, String url) {
        android.view.View button = findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW,
                        android.net.Uri.parse(url));
                startActivity(intent);
            });
        }
    }

    private void setupMovieClick(int buttonId, String movieTitle) {
        android.view.View button = findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(this, SeatSelectionActivity.class);
                intent.putExtra("MOVIE_TITLE", movieTitle);
                startActivity(intent);
            });
        }
    }

    private void setupDateToggle() {
        android.widget.TextView btnToday = findViewById(R.id.btnDateToday);
        android.widget.TextView btnTomorrow = findViewById(R.id.btnDateTomorrow);

        if (btnToday != null && btnTomorrow != null) {
            btnToday.setOnClickListener(v -> updateDateSelection(true, btnToday, btnTomorrow));
            btnTomorrow.setOnClickListener(v -> updateDateSelection(false, btnToday, btnTomorrow));
        }
    }

    private void updateDateSelection(boolean isToday, android.widget.TextView btnToday,
            android.widget.TextView btnTomorrow) {
        if (isToday) {
            btnToday.setText("◉ Today");
            btnToday.setTextColor(getResources().getColor(R.color.white));
            btnToday.setBackgroundResource(R.drawable.bg_radio_selected);
            btnToday.setTypeface(null, android.graphics.Typeface.BOLD);

            btnTomorrow.setText("○ Tomorrow");
            btnTomorrow.setTextColor(getResources().getColor(R.color.text_gray));
            btnTomorrow.setBackgroundResource(R.drawable.bg_radio_unselected);
            btnTomorrow.setTypeface(null, android.graphics.Typeface.NORMAL);
        } else {
            btnToday.setText("○ Today");
            btnToday.setTextColor(getResources().getColor(R.color.text_gray));
            btnToday.setBackgroundResource(R.drawable.bg_radio_unselected);
            btnToday.setTypeface(null, android.graphics.Typeface.NORMAL);

            btnTomorrow.setText("◉ Tomorrow");
            btnTomorrow.setTextColor(getResources().getColor(R.color.white));
            btnTomorrow.setBackgroundResource(R.drawable.bg_radio_selected);
            btnTomorrow.setTypeface(null, android.graphics.Typeface.BOLD);
        }
    }
}
