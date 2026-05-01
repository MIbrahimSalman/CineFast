package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class DrawerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navView;

    private Movie currentMovie;
    private int currentSeatCount;
    private ArrayList<String> currentSelectedSeats;
    private double currentTicketPrice;

    private static final String PREFS_NAME = "cinefast_session_pref_v3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    navigateToHome();
                } else if (id == R.id.nav_my_bookings) {
                    // navigateToMyBookings(); // To be implemented in Commit 5
                    Toast.makeText(DrawerActivity.this, "My Bookings coming in Commit 5", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_logout) {
                    logout();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), (String) null);
            navView.setCheckedItem(R.id.nav_home);
            Toast.makeText(this, "CineFAST", Toast.LENGTH_SHORT).show();
        }
    }

    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit().clear().apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Navigation methods from NewMainActivity

    public void navigateToSeatSelection(Movie movie) {
        this.currentMovie = movie;
        SeatSelectionFragment fragment = new SeatSelectionFragment();
        Bundle args = new Bundle();
        args.putSerializable("MOVIE", movie);
        fragment.setArguments(args);
        loadFragment(fragment, true);
    }

    public void navigateToSnacks(String movieTitle, int seatCount,
            ArrayList<String> selectedSeats, double ticketPrice) {
        this.currentSeatCount = seatCount;
        this.currentSelectedSeats = selectedSeats;
        this.currentTicketPrice = ticketPrice;

        SnacksFragment fragment = new SnacksFragment();
        Bundle args = new Bundle();
        args.putString("MOVIE_TITLE", movieTitle);
        args.putInt("SEAT_COUNT", seatCount);
        args.putStringArrayList("SELECTED_SEATS", selectedSeats);
        args.putDouble("TICKET_PRICE", ticketPrice);
        fragment.setArguments(args);
        loadFragment(fragment, true);
    }

    public void navigateToTicketSummary(String movieTitle, int seatCount,
            ArrayList<String> selectedSeats,
            double ticketPrice, double snacksTotal,
            double finalTotal,
            int qtyPopcorn, int qtyNachos,
            int qtyDrink, int qtyCandy) {
        TicketSummaryFragment fragment = new TicketSummaryFragment();
        Bundle args = new Bundle();
        args.putString("MOVIE_TITLE", movieTitle);
        args.putInt("SEAT_COUNT", seatCount);
        args.putStringArrayList("SELECTED_SEATS", selectedSeats);
        args.putDouble("TICKET_TOTAL", ticketPrice);
        args.putDouble("SNACKS_TOTAL", snacksTotal);
        args.putDouble("FINAL_TOTAL", finalTotal);
        args.putInt("QTY_POPCORN", qtyPopcorn);
        args.putInt("QTY_NACHOS", qtyNachos);
        args.putInt("QTY_DRINK", qtyDrink);
        args.putInt("QTY_CANDY", qtyCandy);
        fragment.setArguments(args);
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        loadFragment(fragment, (String) null);
    }

    public void navigateToHome() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        loadFragment(new HomeFragment(), (String) null);
        navView.setCheckedItem(R.id.nav_home);
    }

    private void loadFragment(Fragment fragment, String backStackTag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentContainer, fragment);
        if (backStackTag != null) {
            ft.addToBackStack(backStackTag);
        }
        ft.commit();
    }

    private void loadFragment(Fragment fragment, boolean addToBackStack) {
        loadFragment(fragment, addToBackStack ? "" : null);
    }

    public Movie getCurrentMovie() {
        return currentMovie;
    }

    public int getCurrentSeatCount() {
        return currentSeatCount;
    }

    public ArrayList<String> getCurrentSelectedSeats() {
        return currentSelectedSeats;
    }

    public double getCurrentTicketPrice() {
        return currentTicketPrice;
    }
}
