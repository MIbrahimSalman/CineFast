package com.example.cinefast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class SnacksFragment extends Fragment {

    private String movieTitle;
    private int seatCount;
    private ArrayList<String> selectedSeats;
    private double ticketPrice;

    private SnackAdapter snackAdapter;
    private TextView textSnackTotal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_snacks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            movieTitle = getArguments().getString("MOVIE_TITLE");
            seatCount = getArguments().getInt("SEAT_COUNT", 0);
            selectedSeats = getArguments().getStringArrayList("SELECTED_SEATS");
            ticketPrice = getArguments().getDouble("TICKET_PRICE", 0.0);
        }

        view.findViewById(R.id.btnBack)
                .setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        textSnackTotal = view.findViewById(R.id.textSnackTotal);

        // Build snack list from SQLite
        SnackDbHelper dbHelper = new SnackDbHelper(requireContext());
        List<Snack> snacks = dbHelper.getAllSnacks();

        snackAdapter = new SnackAdapter(requireContext(), snacks, this::updateTotal);

        ListView listView = view.findViewById(R.id.listViewSnacks);
        listView.setAdapter(snackAdapter);

        updateTotal();

        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(v -> {
            if (getActivity() instanceof DrawerActivity) {
                double snacksTotal = snackAdapter.getTotalSnackCost();
                double finalTotal = ticketPrice + snacksTotal;
                ((DrawerActivity) getActivity()).navigateToTicketSummary(
                        movieTitle, seatCount, selectedSeats,
                        ticketPrice, snacksTotal, finalTotal,
                        snackAdapter.getQty(0), snackAdapter.getQty(1),
                        snackAdapter.getQty(2), snackAdapter.getQty(3));
            }
        });
    }

    private void updateTotal() {
        if (textSnackTotal != null && snackAdapter != null) {
            double total = snackAdapter.getTotalSnackCost();
            textSnackTotal.setText(String.format(java.util.Locale.getDefault(),
                    "Snacks Total: $%.2f", total));
        }
    }
}
