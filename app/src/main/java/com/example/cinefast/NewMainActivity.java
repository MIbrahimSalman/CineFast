package com.example.cinefast;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class NewMainActivity extends AppCompatActivity {

    private Movie currentMovie;
    private int currentSeatCount;
    private ArrayList<String> currentSelectedSeats;
    private double currentTicketPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), (String) null);
        }
    }

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
