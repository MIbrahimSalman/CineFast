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

        recyclerBookings.setLayoutManager(new LinearLayoutManager(requireContext()));
        bookingList = new ArrayList<>();
        adapter = new BookingAdapter(bookingList);
        recyclerBookings.setAdapter(adapter);

        loadBookings();
    }

    private void loadBookings() {
        SharedPreferences prefs = requireContext().getSharedPreferences("cinefast_session_pref_v3", android.content.Context.MODE_PRIVATE);
        String uid = prefs.getString("uid", null);

        if (uid == null) {
            textNoBookings.setVisibility(View.VISIBLE);
            return;
        }

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
            }
        });
    }
}
