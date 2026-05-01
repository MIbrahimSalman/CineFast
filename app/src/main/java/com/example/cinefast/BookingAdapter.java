package com.example.cinefast;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookingList;

    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.textMovieTitle.setText(booking.getMovieTitle());
        holder.textSeats.setText(booking.getSeatCount() + " Tickets");

        // Figma shows format like 13.04.2025,  22:15
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault());
        if (booking.getShowTimestamp() > 0) {
            holder.textDate.setText(sdf.format(new Date(booking.getShowTimestamp())));
        } else {
            holder.textDate.setText(sdf.format(new Date(booking.getTimestamp())));
        }

        // Cancel logic
        long currentTime = System.currentTimeMillis();

        // If showTimestamp is set and the movie has started/passed
        if (booking.getShowTimestamp() > 0 && currentTime > booking.getShowTimestamp()) {
            holder.btnCancel.setEnabled(false);
            holder.btnCancel.setAlpha(0.3f);
        } else {
            holder.btnCancel.setEnabled(true);
            holder.btnCancel.setAlpha(1.0f);
            holder.btnCancel.setOnClickListener(v -> {
                new android.app.AlertDialog.Builder(v.getContext())
                        .setTitle("Cancel Booking")
                        .setMessage("Are you sure you want to cancel this booking?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("bookings")
                                    .child(booking.getUserId())
                                    .child(booking.getId());
                            ref.removeValue().addOnSuccessListener(aVoid -> {
                                Toast.makeText(v.getContext(), "Booking cancelled", Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(e -> {
                                Toast.makeText(v.getContext(), "Failed to cancel", Toast.LENGTH_SHORT).show();
                            });
                        })
                        .setNegativeButton("No", null)
                        .show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView textMovieTitle, textSeats, textDate;
        android.widget.ImageButton btnCancel;

        BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            textMovieTitle = itemView.findViewById(R.id.textMovieTitle);
            textSeats = itemView.findViewById(R.id.textSeats);
            textDate = itemView.findViewById(R.id.textDate);
            btnCancel = itemView.findViewById(R.id.btnCancelBooking);
        }
    }
}
