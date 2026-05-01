package com.example.cinefast;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyBookingsFragment extends Fragment {

    private RecyclerView recyclerBookings;
    private TextView textNoBookings;
    private BookingAdapter adapter;
    private List<Booking> bookingList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_bookings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerBookings = view.findViewById(R.id.recyclerBookings);
        textNoBookings = view.findViewById(R.id.textNoBookings);

        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            if (getActivity() instanceof DrawerActivity) {
                ((DrawerActivity) getActivity()).navigateToHome();
            }
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

        recyclerBookings.setLayoutManager(new LinearLayoutManager(requireContext()));
        bookingList = new ArrayList<>();
        adapter = new BookingAdapter(bookingList);
        recyclerBookings.setAdapter(adapter);

        loadBookings();
    }

    private void loadBookings() {
        com.google.firebase.auth.FirebaseUser user = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            textNoBookings.setVisibility(View.VISIBLE);
            return;
        }
        String uid = user.getUid();
        
        // Add a dummy future booking once for testing
        addDummyBookingOnce(uid);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("bookings").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Booking booking = dataSnapshot.getValue(Booking.class);
                    if (booking != null) {
                        bookingList.add(booking);
                    }
                }
                
                // Sort by latest first
                Collections.sort(bookingList, (b1, b2) -> Long.compare(b2.getTimestamp(), b1.getTimestamp()));
                
                adapter.notifyDataSetChanged();

                if (bookingList.isEmpty()) {
                    textNoBookings.setVisibility(View.VISIBLE);
                    recyclerBookings.setVisibility(View.GONE);
                } else {
                    textNoBookings.setVisibility(View.GONE);
                    recyclerBookings.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (getContext() != null) {
                    android.widget.Toast.makeText(getContext(), "Failed to load bookings: " + error.getMessage(), android.widget.Toast.LENGTH_LONG).show();
                    android.util.Log.e("FirebaseBooking", "Error loading bookings", error.toException());
                }
            }
        });
    }

    private void addDummyBookingOnce(String uid) {
        SharedPreferences prefs = requireContext().getSharedPreferences("cinefast_session_pref_v3", android.content.Context.MODE_PRIVATE);
        boolean added = prefs.getBoolean("dummy_future_added", false);
        if (!added) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("bookings").child(uid);
            String bookingId = ref.push().getKey();
            if (bookingId != null) {
                long futureShow = System.currentTimeMillis() + (5L * 24L * 60L * 60L * 1000L); // 5 days in future
                List<String> seats = new ArrayList<>();
                seats.add("1_2");
                seats.add("1_3");
                // Using the updated constructor: id, userId, movieTitle, seatCount, selectedSeats, finalTotal, timestamp, showTimestamp, movieDate, movieTime
                Booking dummy = new Booking(bookingId, uid, "Future Dummy Movie", 2, seats, 24.0, System.currentTimeMillis(), futureShow, "DummyDate", "DummyTime");
                ref.child(bookingId).setValue(dummy);
            }
            prefs.edit().putBoolean("dummy_future_added", true).apply();
        }
    }
}
